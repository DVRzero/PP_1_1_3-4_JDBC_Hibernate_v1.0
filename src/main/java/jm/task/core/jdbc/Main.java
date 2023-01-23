package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

// требуется где необходимо реализовать логику транзакций - X
// поля класса должны быть приватными - X
// используй полиморфизм - обращайся к классу через интерфейс - X
// при создании таблицы нужно проверить, что она существует, также желательно при удалении - X

public class Main {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Name1", "LastName1", (byte) 10);
        userService.saveUser("Name2", "LastName2", (byte) 20);
        userService.saveUser("Name3", "LastName3", (byte) 30);
        userService.saveUser("Name4", "LastName4", (byte) 40);

        userService.removeUserById(1);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
