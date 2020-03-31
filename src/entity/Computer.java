package entity;

public class Computer {
    private int id;
    private String name;
    private String cpu;
    private Long cpuUsage;
    private String memory;
    private Long memoryUsage;
    private String disk;
    private Long diskUsage;
    private String network;
    private Long networkUsage;

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

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public Long getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Long cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public Long getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public Long getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Long diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Long getNetworkUsage() {
        return networkUsage;
    }

    public void setNetworkUsage(Long networkUsage) {
        this.networkUsage = networkUsage;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cpu='" + cpu + '\'' +
                ", cpuUsage=" + cpuUsage +
                ", memory='" + memory + '\'' +
                ", memoryUsage=" + memoryUsage +
                ", disk='" + disk + '\'' +
                ", diskUsage=" + diskUsage +
                ", network='" + network + '\'' +
                ", networkUsage=" + networkUsage +
                '}';
    }
}
