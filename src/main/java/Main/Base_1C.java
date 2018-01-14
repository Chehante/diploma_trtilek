package Main;

public class Base_1C {

    private String user;
    private String password;
    private String name;
    private int indexFromTable;
    private boolean sessionsDenied;
    private boolean scheduledJobsDenied;

    public boolean isSessionsDenied() {
        return sessionsDenied;
    }

    public void setSessionsDenied(boolean sessionsDenied) {
        this.sessionsDenied = sessionsDenied;
    }

    public boolean isScheduledJobsDenied() {
        return scheduledJobsDenied;
    }

    public void setScheduledJobsDenied(boolean scheduledJobsDenied) {
        this.scheduledJobsDenied = scheduledJobsDenied;
    }

    public int getIndexFromTable() {
        return indexFromTable;
    }

    public void setIndexFromTable(int indexFromTable) {
        this.indexFromTable = indexFromTable;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
