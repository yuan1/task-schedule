package entity;

public class Computer {
    private int id;
    private String name;
    private String cpu;
    private Long cpuUsage;
    private String Memory;
    private Long MemoryUsage;
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
        return Memory;
    }

    public void setMemory(String memory) {
        Memory = memory;
    }

    public Long getMemoryUsage() {
        return MemoryUsage;
    }

    public void setMemoryUsage(Long memoryUsage) {
        MemoryUsage = memoryUsage;
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
                ", CPU='" + cpu + '\'' +
                ", CPUUsage=" + cpuUsage +
                ", Memory='" + Memory + '\'' +
                ", MemoryUsage=" + MemoryUsage +
                ", disk='" + disk + '\'' +
                ", diskUsage=" + diskUsage +
                ", network='" + network + '\'' +
                ", networkUsage=" + networkUsage +
                '}';
    }
}
