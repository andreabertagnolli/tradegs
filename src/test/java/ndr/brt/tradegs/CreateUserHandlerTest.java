package ndr.brt.tradegs;

import ndr.brt.tradegs.user.CreateUser;
import ndr.brt.tradegs.user.CreateUserHandler;
import ndr.brt.tradegs.user.User;
import ndr.brt.tradegs.user.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CreateUserHandlerTest {

    private Users users = mock(Users.class);
    private CreateUserHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreateUserHandler(users);
    }

    @Test
    void save_user() {
        when(users.get("user")).thenReturn(new User());

        handler.consume(new CreateUser("user"));

        verify(users).save(argThat(user -> "user".equals(user.id())));
    }

    @Test
    void when_user_already_exists_does_nothing() {
        User user = new User();
        user.created("user");
        when(users.get("user")).thenReturn(user);

        handler.consume(new CreateUser("user"));

        verify(users, never()).save(any());
    }
}