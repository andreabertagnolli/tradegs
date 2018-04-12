package ndr.brt.tradegs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Optional.empty;
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
        when(users.get("user")).thenReturn(empty());

        handler.handle(new CreateUser("user"));

        verify(users).save(argThat(user -> "user".equals(user.id())));
    }

    @Test
    void when_user_already_exists_does_nothing() {
        when(users.get("user")).thenReturn(Optional.of(new User()));

        handler.handle(new CreateUser("user"));

        verify(users, never()).save(any());
    }
}