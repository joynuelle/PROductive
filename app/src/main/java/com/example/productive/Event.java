package com.example.productive;

public class Event {
        private String date;
        private String name;
        private String description;

        public Event(String date, String name, String description) {
            this.date = date;
            this.name = name;
            this.description = description;
        }

        public String getDate() { return date; }
        public String getTitle() { return name; }
        public String getContent() { return description; }

}
