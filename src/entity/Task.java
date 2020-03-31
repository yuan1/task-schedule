package entity;

public class Task {
    private int id;
    private String name;
    private Long cpuUsage;
    private Long memoryUsage;
    private Long diskUsage;
    private Long networkUsage;
    private Long timeUsage;
    private Long createTime;
    private int complete; // 是否完成
    private Long completeTime;
    private int computerId;
    private String computerName;
    private String status; // 状态

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Long cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Long getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Long getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Long diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Long getNetworkUsage() {
        return networkUsage;
    }

    public void setNetworkUsage(Long networkUsage) {
        this.networkUsage = networkUsage;
    }

    public Long getTimeUsage() {
        return timeUsage;
    }

    public void setTimeUsage(Long timeUsage) {
        this.timeUsage = timeUsage;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }

    public int getComputerId() {
        return computerId;
    }

    public void setComputerId(int computerId) {
        this.computerId = computerId;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                ", diskUsage=" + diskUsage +
                ", networkUsage=" + networkUsage +
                ", timeUsage=" + timeUsage +
                ", createTime=" + createTime +
                ", complete=" + complete +
                ", completeTime=" + completeTime +
                ", computerId=" + computerId +
                ", computerName='" + computerName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
