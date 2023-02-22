package com.contentwire.service.repository.service;

import com.contentwire.model.*;
import com.contentwire.service.resource.management.UserManagementService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A Singleton for interacting with SQL databases to manage E-Mail marketing workflows within an organization.
 */

public class ConnectionService {

    private static Connection connection;

    /**
     * Set up the DB Connection for a given URI.
     * @param URI Database URI
     * @throws ClassNotFoundException ignored
     * @throws SQLException The Database Connection cannot be set up.
     */

    public static void setConnectionURI(String URI) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(URI, "postgres", "admin5432");
    }

    /**
     * It initializes tables for a ContentWIRE application.
     * @throws SQLException The requested tables (or other entities) cannot be set up.
     */
    public static void initTables() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(SQLCommands.CREATE_TABLES);
        insertRootUser();
    }

    /**
     * It deletes an Audience from the database. The DB removes the Audience's relationships with other records as well.
     * @param audienceName an Audience uniquely identified by its name
     * @throws SQLException the database operations cannot be executed.
     */
    public static void deleteAudience(String audienceName) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.DELETE_AUDIENCE)) {
            stmt.setString(1, audienceName);
            stmt.executeQuery();
        }
    }

    /**
     * It inserts a root user for the ContentWIRE System.
     * @throws SQLException the database operations cannot be executed.
     */
    private static void insertRootUser() throws SQLException {
        CampaignOrchestrator root = new CampaignOrchestrator("root", UserManagementService.genHash("root"), true);
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.INSERT_ROOT)) {
                stmt.setString(1, root.getUserName());
                stmt.setString(2, root.getPasswordHash());
                stmt.execute();
            }
    }

    /**
     * It deletes an Audience from the database. The DB removes the Campaign's relationships with other records as well.
     * @param campaignName a Campaign uniquely identified by its name
     * @throws SQLException the database operations cannot be executed.
     */

    public static void deleteCampaign(String campaignName) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.DELETE_CAMPAIGN)) {
            stmt.setString(1, campaignName);
            stmt.executeQuery();
        }
    }

    /**
     * Retrieves an Organization Member from the database
     * @param username A Campaign Manager uniquely identified by its username
     * @return
     * @throws SQLException
     */

    public static CampaignManager getUserByName(String username) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.SELECT_USER_BY_NAME)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                String passwordHash = rs.getString(3);
                CampaignManager cm = new CampaignManager(username, passwordHash);
                cm.setDisplayMode(getDisplayMode(username));
                return cm;
            } else {
                throw new SQLDataException("User not found");
            }
        }
    }


    /**
     * Retrieves all audience members from the database.
     * @param audienceName an audience uniquely identified by its username
     * @return the list of audience members that belong to the audience.
     * @throws SQLException the database commands cannot be executed.
     */

    public static List<AudienceMember> getMembersOfAudience(String audienceName) throws SQLException {
        List<AudienceMember> audienceMembers = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.SELECT_MEMBERS_OF_AUDIENCE)) {
            stmt.setString(1, audienceName);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                AudienceMember a = new AudienceMember(rs.getString("email_address"),rs.getString("full_name"));
                audienceMembers.add(a);
            }
        }

        return audienceMembers;
    }

    /**
     * Retrieves audiences that a particular audience member is part of.
     * @param member_email name of the audience member
     * @return a list of audiences that the member is part of
     * @throws SQLException the query couldn't be processed
     */

    public static List<Audience> getAudienceMembershipsOfMember(String member_email) throws SQLException {

        List<Audience> audiences = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.SELECT_AUDIENCE_MEMBERSHIPS)) {
            stmt.setString(1, member_email);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Audience a = new Audience();
                a.setName(rs.getString("name"));
                audiences.add(a);
            }
        }

        return audiences;
    }

    /**
     * Inserts a new Audience into the database.
     * @param a audience to insert
     * @throws SQLException the query couldn't be processed.
     */

    public static void insertAudience(Audience a) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.INSERT_AUDIENCE)) {
            stmt.setString(1, a.getName());
            stmt.setString(2, a.getDescription());
            stmt.execute();
        }
    }

    /**
     * Retrieves all audience members stored in the database.
     * @return a list containing all audience members
     * @throws SQLException the query couldn't be processed
     */

    public static List<AudienceMember> getAudienceMembers() throws SQLException {
        List<AudienceMember> ams = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SQLCommands.SELECT_AUDIENCE_MEMBERS);
        while(rs.next()) {
            ams.add(new AudienceMember(rs.getString("email_address"), rs.getString("full_name")));
        }
        return ams;
    }

    /**
     * Retrieves all audiences stored in the database
     * @return a list containing all audiences
     * @throws SQLException the query couldn't be processed
     */

    public static List<Audience> getAudiences() throws SQLException {
        List<Audience> audiences = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SQLCommands.SELECT_AUDIENCES);
        while(rs.next()) {
            Audience a = new Audience();
            a.setName(rs.getString("name"));
            a.setDescription(rs.getString("description"));
            audiences.add(a);
        }
        return audiences;
    }

    /**
     * Retrieves all campaigns stored in the database.
     * @return a list containing every Campaign.
     * @throws SQLException the query couldn't be processed.
     */

    public static List<Campaign> getCampaigns() throws SQLException {
        List<Campaign> campaigns = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SQLCommands.SELECT_CAMPAIGNS);
        while(rs.next()) {
            Campaign campaign = new Campaign();
            campaign.setName(rs.getString("name"));
            campaign.setDescription(rs.getString("description"));
            campaigns.add(campaign);
        }
        return campaigns;
    }

    /**
     * Retrieve Campaigns that a Campaign Responsible is assigned to.
     * @param username the username of the user
     * @return a list containing all Campaigns the user is assigned to.
     * @throws SQLException the query couldn't be processed.
     */
    public static List<Campaign> getCampaignsOfCampaignManager(String username) throws SQLException {

        List<Campaign> campaigns = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.SELECT_CAMPAIGNS_OF_MANAGER)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Campaign campaign = new Campaign();
                campaign.setName(rs.getString("name"));
                campaigns.add(campaign);
            }
        }

        return campaigns;
    }

    /**
     * Delete a user from the database.
     * @param userName username of the user you want to delete
     * @throws SQLException the query couldn't be processed.
     */

    public static void deleteUser(String userName) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.DELETE_CAMPAIGN_MANAGER)) {
            stmt.setString(1, userName);
            stmt.executeQuery();
        }
    }

    /**
     * Delete an Audience Member from the database.
     * @param email E-Mail address of the audience member you want to delete
     * @throws SQLException the query couldn't be processed
     */
    public static void deleteAudienceMember(String email) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.DELETE_AUDIENCE_MEMBER)) {
            stmt.setString(1, email);
            stmt.executeQuery();
        }
    }

    /**
     * Retrieves the display mode for a user.
     * @param name username of the Campaign Manager in question
     * @return the DisplayMode of the user
     * @throws SQLException the query couldn't be processed
     */

    public static DisplayMode getDisplayMode(String name) throws SQLException {
            try(PreparedStatement stmt = connection.prepareStatement(SQLCommands.IS_CAMPAIGN_ORCHESTRATOR)) {
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                if(rs.getBoolean(1)) {
                    return DisplayMode.ORCHESTRATOR;
                } else {
                    return DisplayMode.RESPONSIBLE;
                }
            }
    }

    /**
     * Inserts a new Campaign Orchestrator into the database.
     * @param co the Campaign Orchestrator you want to insert
     * @throws SQLException the query couldn't be processed
     */
    public static void insertCampaignOrchestrator(CampaignOrchestrator co) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.INSERT_CAMPAIGN_ORCHESTRATOR)) {
            stmt.setString(1, co.getUserName());
            stmt.setString(2, co.getPasswordHash());
            stmt.setString(3, co.getUserName());
            stmt.execute();
        }
    }

    /**
     * Inserts a new Campaign Responsible into the database.
     * @param cr the Campaign Responsible you want to insert
     * @throws SQLException the query couldn't be processed
     */
    public static void insertCampaignResponsible(CampaignResponsible cr) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.INSERT_CAMPAIGN_RESPONSIBLE)) {
            stmt.setString(1, cr.getUserName());
            stmt.setString(2, cr.getPasswordHash());
            stmt.setString(3, cr.getUserName());
            stmt.execute();
        }
    }

    /**
     * Retrieve the Audiences that belong to a campaign
     * @param campaignName the name of the campaign
     * @return a list containing all the audiences that are part of the campaign
     * @throws SQLException the query couldn't be processed
     */
    public static List<Audience> getAudiencesForCampaign(String campaignName) throws SQLException {
        List<Audience> audiences = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.SELECT_CAMPAIGN_AUDIENCES)) {
            stmt.setString(1, campaignName);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                audiences.add(new Audience(rs.getString("name"), rs.getString("description")));
            }
        }

        return audiences;
    }

    /**
     * Insert a new audience member into the database.
     * @param am the audience member to insert.
     * @throws SQLException the query couldn't be processed
     */
    public static void insertAudienceMember(AudienceMember am) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.INSERT_AUDIENCE_MEMBER)) {
            stmt.setString(1, am.getEmail());
            stmt.setString(2, am.getName());
            stmt.execute();
        }
    }

    /**
     * Retrieve all Campaign Managers that belong to the organization
     * @return a list containing all Campaign Managers
     * @throws SQLException the query couldn't be processed
     */

    public static List<CampaignManager> getOrganizationMembers() throws SQLException {
        List<CampaignManager> campaignManagers = new ArrayList<>();
        campaignManagers.addAll(getCampaignOrchestrators());
        campaignManagers.addAll(getCampaignResponsibles());

        return campaignManagers;
    }

    /**
     * Retrieve all Campaign Responsibles that belong to the organization
     * @return a list containing all Campaign Responsibles
     * @throws SQLException the query couldn't be processed
     */

    public static List<CampaignResponsible> getCampaignResponsibles() throws SQLException {
        List<CampaignResponsible> cos = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SQLCommands.SELECT_CAMPAIGN_RESPONSIBLES);
        while(rs.next()) {
            cos.add(new CampaignResponsible(rs.getString("username"), rs.getString("passwordhash")));
        }
        return cos;
    }

    /**
     * Insert a new campaign into the database.
     * @param campaign the campaign to insert.
     * @throws SQLException the query couldn't be processed
     */

    public static void insertCampaign(Campaign campaign) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.INSERT_CAMPAIGN)) {
            stmt.setString(1, campaign.getName());
            stmt.setString(2, campaign.getDescription());
            stmt.execute();
        }
    }

    /**
     * Retrieve all Campaign Orchesterators that belong to the organization
     * @return a list containing all Campaign Orchesterators
     * @throws SQLException the query couldn't be processed
     */

    public static List<CampaignOrchestrator> getCampaignOrchestrators() throws SQLException {
        List<CampaignOrchestrator> cos = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SQLCommands.SELECT_CAMPAIGN_ORCHESTRATORS);
        while(rs.next()) {
            cos.add(new CampaignOrchestrator(rs.getString("username"), rs.getString("passwordhash"), rs.getBoolean("is_root")));
        }
        return cos;
    }

    /**
     * Adds the audience to a certain campaign
     * @param campaignName the name of the campaign
     * @param audienceName the name of the audience
     * @throws SQLException the query couldn't be processed
     */

    public static void setCampaignAudience(String campaignName, String audienceName) throws SQLException{
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.INSERT_CAMPAIGN_AUDIENCE)) {
            stmt.setString(1, campaignName);
            stmt.setString(2, audienceName);
            stmt.execute();
        }
    }

    /**
     * Removes an Audience - Campaign relationship from the database.
     * @param campaignName name of the campaign
     * @param audienceName name of the audience
     * @throws SQLException the query couldn't be processed
     */
    public static void removeCampaignAudience(String campaignName, String audienceName) throws SQLException{
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.REMOVE_CAMPAIGN_AUDIENCE)) {
            stmt.setString(1, campaignName);
            stmt.setString(2, audienceName);
            stmt.execute();
        }
    }

    /**
     *  Add a new Audience Member - Audience membership to the database
     * @param audienceName name of the audience
     * @param memberEmail E-Mail address of member
     * @throws SQLException the query couldn't be processed
     */

    public static void setAudienceMembership(String audienceName, String memberEmail) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.SET_AUDIENCE_MEMBERSHIP)) {
            stmt.setString(1, audienceName);
            stmt.setString(2, memberEmail);
            stmt.execute();
        }
    }

    /**
     * Removes an Audience Member - Audience membership from the database
     * @param audienceName name of the audience
     * @param memberEmail E-Mail address of member
     * @throws SQLException the query couldn't be processed
     */
    public static void removeAudienceMembership(String audienceName, String memberEmail) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.REMOVE_AUDIENCE_MEMBERSHIP)) {
            stmt.setString(1, audienceName);
            stmt.setString(2, memberEmail);
            stmt.execute();
        }
    }

    /**
     * Adds a Camopaign Responsible - Campaign relationship to the database
     * @param campaignName name of the campaign
     * @param username name of the user
     * @throws SQLException the query couldn't be processed
     */
    public static void addCampaignToResponsible(String campaignName, String username) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.ADD_CAMPAIGN_TO_RESPONSIBLE)) {
            stmt.setString(1, campaignName);
            stmt.setString(2, username);
            stmt.execute();
        }
    }

    /**
     * Removes a Campaign Responsible - Campaign relationship from the database
     * @param campaignName name of the campaign
     * @param username name of the user
     * @throws SQLException the query couldn't be processed
     */
    public static void removeCampaignFromResponsible(String campaignName, String username) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLCommands.REMOVE_CAMPAIGN_FROM_RESPONSIBLE)) {
            stmt.setString(1, campaignName);
            stmt.setString(2, username);
            stmt.execute();
        }
    }

}
