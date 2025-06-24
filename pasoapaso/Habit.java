public class Habit {
    private int id;
    private String name;
    private String category;
    private String description;
    private String reminderType; // "daily", "specific_date", "end_date"
    private String reminderTime;
    private String endDate;
    
    public Habit(String name, String category, String description, 
                String reminderType, String reminderTime, String endDate) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.reminderType = reminderType;
        this.reminderTime = reminderTime;
        this.endDate = endDate;
    }
    
    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getReminderType() { return reminderType; }
    public String getReminderTime() { return reminderTime; }
    public String getEndDate() { return endDate; }
}