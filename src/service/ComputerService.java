package service;

import dao.ComputerDAO;
import entity.Computer;

import java.util.List;

public class ComputerService {
    private ComputerDAO computerDAO = new ComputerDAO();

    public List<Computer> selectAll() {
        return computerDAO.selectAll();
    }
    public Computer selectOne(int id) {
        return computerDAO.selectOne(id);
    }

    public Boolean save(Computer computer) {
        computer.setDiskUsage(0L);
        computer.setMemoryUsage(0L);
        computer.setNetworkUsage(0L);
        computer.setCpuUsage(0L);
        return computerDAO.save(computer);
    }

}
