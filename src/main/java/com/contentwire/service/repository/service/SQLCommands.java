package com.contentwire.service.repository.service;

public class SQLCommands {
    public final static String CREATE_TABLES = "CREATE TABLE IF NOT EXISTS users\n" +
            "\t(id SERIAL PRIMARY KEY, username TEXT UNIQUE NOT NULL, passwordHash TEXT);\n" +
            "\t\n" +
            "CREATE TABLE IF NOT EXISTS campaign_responsibles\n" +
            "\t(id SERIAL PRIMARY KEY,\n" +
            "\tCONSTRAINT cr_fk\n" +
            "\t\tFOREIGN KEY(id)\n" +
            "\t\tREFERENCES users(id)\n" +
            "\t\tON DELETE CASCADE);\n" +
            "\t\n" +

            "CREATE TABLE IF NOT EXISTS campaign_orchestrators\n" +
            "\t(id SERIAL PRIMARY KEY, is_root BOOLEAN,\n" +
            "\tCONSTRAINT co_fk\n" +
            "\t\tFOREIGN KEY(id)\n" +
            "\t\tREFERENCES users(id)\n" +
            "\t\tON DELETE CASCADE);\n" +
            "\t\n" +

            "CREATE TABLE IF NOT EXISTS campaigns\n" +
            "\t(id SERIAL PRIMARY KEY, name TEXT UNIQUE NOT NULL, description TEXT);\n" +
            "\t\n" +
            "CREATE TABLE IF NOT EXISTS audiences\n" +
            "\t(id SERIAL PRIMARY KEY, name TEXT UNIQUE NOT NULL, description TEXT);\n" +
            "\t\n" +
            "CREATE TABLE IF NOT EXISTS audience_members\n" +
            "\t(id SERIAL PRIMARY KEY, email_address TEXT UNIQUE NOT NULL, full_name TEXT);\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS cr_campaigns\n" +
            "\t(cr_id SERIAL, camp_id SERIAL,\n" +
            "\tCONSTRAINT cr_camp_fk\n" +
            "\t\tFOREIGN KEY(cr_id)\n" +
            "\t\tREFERENCES campaign_responsibles(id)\n" +
            "\t\tON DELETE CASCADE,\n" +
            "\tCONSTRAINT camp_cr_fk\n" +
            "\t\tFOREIGN KEY(camp_id)\n" +
            "\t\tREFERENCES campaigns(id)\n" +
            "\t\tON DELETE CASCADE);\n" +
            "\t\t\n" +
            "CREATE TABLE IF NOT EXISTS campaign_audiences\n" +
            "\t(camp_id SERIAL, audience_id SERIAL,\n" +
            "\tCONSTRAINT aud_camp_fk\n" +
            "\t\tFOREIGN KEY(audience_id)\n" +
            "\t\tREFERENCES audiences(id)\n" +
            "\t\tON DELETE CASCADE,\n" +
            "\tCONSTRAINT camp_aud_fk\n" +
            "\t\tFOREIGN KEY(camp_id)\n" +
            "\t\tREFERENCES campaigns(id)\n" +
            "\t\tON DELETE CASCADE);\n" +
            "\t\t\n" +
            "CREATE TABLE IF NOT EXISTS audience_memberships\n" +
            "\t(audience_id SERIAL, member_id SERIAL,\n" +
            "\tCONSTRAINT aud_mem_fk\n" +
            "\t\tFOREIGN KEY(audience_id)\n" +
            "\t\tREFERENCES audiences(id)\n" +
            "\t\tON DELETE CASCADE,\n" +
            "\tCONSTRAINT mem_aud_fk\n" +
            "\t\tFOREIGN KEY(member_id)\n" +
            "\t\tREFERENCES audience_members(id)\n" +
            "\t\tON DELETE CASCADE);\n";

    public final static String INSERT_ROOT = "INSERT INTO users(username, passwordhash)" +
            "VALUES(?, ?) ON CONFLICT DO NOTHING;" +
            "INSERT INTO campaign_orchestrators(id, is_root)" +
            "(SELECT id, TRUE FROM users WHERE username = 'root')" +
            "ON CONFLICT DO NOTHING;";
    public final static String INSERT_CAMPAIGN_ORCHESTRATOR = "INSERT INTO users(username, passwordhash)" +
            "VALUES(?, ?);" +
            "INSERT INTO campaign_orchestrators(id, is_root)" +
            "(SELECT id, FALSE FROM users WHERE username = ?)";
    public final static String INSERT_CAMPAIGN_RESPONSIBLE = "INSERT INTO users(username, passwordhash)" +
            "VALUES(?, ?);" +
            "INSERT INTO campaign_responsibles(id)" +
            "(SELECT id FROM users WHERE username = ?)";
    public final static String INSERT_AUDIENCE = "INSERT INTO audiences(name, description) VALUES(?, ?);";
    public final static String INSERT_AUDIENCE_MEMBER = "INSERT INTO audience_members(email_address, full_name) VALUES(?, ?);";
    public final static String INSERT_CAMPAIGN = "INSERT INTO campaigns(name, description) VALUES(?, ?);";
    public static final String INSERT_CAMPAIGN_AUDIENCE = "INSERT INTO campaign_audiences(camp_id, audience_id) (SELECT campaigns.id, audiences.id FROM campaigns, audiences WHERE campaigns.name = ? AND audiences.name = ?);";
    public static final String SET_AUDIENCE_MEMBERSHIP = "INSERT INTO audience_memberships(audience_id, member_id) (SELECT audiences.id, audience_members.id FROM audiences, audience_members WHERE audiences.name = ? AND audience_members.email_address = ?);";
    public static final String ADD_CAMPAIGN_TO_RESPONSIBLE = "INSERT INTO cr_campaigns(cr_id, camp_id) (SELECT users.id, campaigns.id FROM users, campaigns WHERE campaigns.name = ? AND users.username = ?);";

    public final static String DELETE_CAMPAIGN_MANAGER = "DELETE FROM users WHERE username = ?";
    public final static String DELETE_CAMPAIGN = "DELETE FROM campaigns WHERE name = ?";
    public final static String DELETE_AUDIENCE = "DELETE FROM audiences WHERE name = ?";
    public final static String DELETE_AUDIENCE_MEMBER = "DELETE FROM audience_members WHERE email_address = ?";
    public final static String REMOVE_CAMPAIGN_AUDIENCE = "DELETE FROM campaign_audiences WHERE (SELECT id FROM campaigns WHERE name = ?) = camp_id AND (SELECT id FROM audiences WHERE name = ?) = audience_id;";
    public final static String REMOVE_AUDIENCE_MEMBERSHIP = "DELETE FROM audience_memberships WHERE (SELECT id FROM audiences WHERE name = ?) = audience_id AND (SELECT id FROM audience_members WHERE email_address = ?) = member_id;";
    public final static String REMOVE_CAMPAIGN_FROM_RESPONSIBLE = "DELETE FROM cr_campaigns WHERE (SELECT id FROM campaigns WHERE name = ?) = cr_campaigns.camp_id AND (SELECT id FROM users WHERE username = ?) = cr_campaigns.cr_id;";

    public final static String SELECT_CAMPAIGN_ORCHESTRATORS = "SELECT users.*, campaign_orchestrators.is_root FROM users, campaign_orchestrators WHERE users.id = campaign_orchestrators.id;";
    public final static String SELECT_CAMPAIGN_RESPONSIBLES = "SELECT users.* FROM users, campaign_responsibles WHERE users.id = campaign_responsibles.id;";
    public final static String SELECT_CAMPAIGNS = "SELECT * FROM campaigns;";
    public final static String SELECT_AUDIENCES = "SELECT * FROM audiences;";
    public final static String SELECT_AUDIENCE_MEMBERS = "SELECT * FROM audience_members;";
    public final static String SELECT_CAMPAIGNS_OF_MANAGER = "SELECT campaigns.* FROM cr_campaigns, users, campaigns WHERE cr_campaigns.camp_id = campaigns.id AND cr_campaigns.cr_id = users.id AND users.username = ?;";
    public final static String SELECT_AUDIENCE_MEMBERSHIPS = "SELECT audiences.* FROM audience_memberships, audience_members, audiences WHERE audience_memberships.audience_id = audiences.id AND audience_memberships.member_id = audience_members.id AND audience_members.email_address = ?;";
    public final static String SELECT_MEMBERS_OF_AUDIENCE = "SELECT audience_members.* FROM audience_members, audiences, audience_memberships WHERE audience_memberships.audience_id = audiences.id AND audience_memberships.member_id = audience_members.id AND audiences.name = ?;";
    public final static String SELECT_USER_BY_NAME = "SELECT * FROM users WHERE username = ?";
    public static final String SELECT_CAMPAIGN_AUDIENCES = "SELECT audiences.* FROM campaigns, audiences, campaign_audiences WHERE campaign_audiences.camp_id = campaigns.id AND campaign_audiences.audience_id = audiences.id AND campaigns.name = ?;";
    public final static String IS_CAMPAIGN_ORCHESTRATOR = "SELECT EXISTS(SELECT * FROM users, campaign_orchestrators WHERE users.username = ? AND users.id = campaign_orchestrators.id);";
}
