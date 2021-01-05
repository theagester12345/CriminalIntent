package com.example.criminalintent.Database;

public class crimeDBSchema {
    public static final class crimeTable {
        public static final String Name="crimes";

        public static final class Cols{
            public static final String uuid = "uuid";
            public static final String title = "title";
            public static final String date = "date";
            public static final String solved = "solved";
            public static final String suspect ="suspect";

        }
    }
}
