package model;

public enum Filter {
    /* for DAO queries */
    CREDENTIALS,
    GROUP_NICKNAME,
    USER_NICKNAME,
    GROUP_OWNER,
    MEETING_OWNER,
    MEETING_ID,
    IP_AND_PORT,

    /* for Loader actions (Pattern Observer) */
    USER_NICK_CHANGE,
    MEETING_JOIN,
    GROUP_JOIN,
    MEETING_CREATION,
    GROUP_CREATION,
}
