package service;

import dao.ComputerDAO;
import entity.Computer;

import java.util.List;

public class ComputerService {
    private ComputerDAO computerDAO = new ComputerDAO();

    public List<Computer> selectAll() {
        return computerDAO.selectAll();
    }
}
