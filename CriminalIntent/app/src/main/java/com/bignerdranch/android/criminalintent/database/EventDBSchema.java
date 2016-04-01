package com.bignerdranch.android.criminalintent.database;

/**
 * Created by vnikolaev on 25.01.2016.
 */
public class EventDBSchema {

    public static final class EventTable {
        public static final String NAME = "events";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String APPROVED = "APPROVED";
        }
    }

    public static final class JoinTable {
        public static final String NAME = "event_participants";

        public static final class Cols {
            public static final String UUID = "e_uuid";
            public static final String PARTICIPANT_UUID = "p_uuid";
        }
    }

    public static final class ParticipantTable {
        public static final String NAME = "participants";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String CONTACT = "contact";
        }
    }
}
