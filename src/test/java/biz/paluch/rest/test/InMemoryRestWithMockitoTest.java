package biz.paluch.rest.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.03.15 08:33
 */
@RunWith(MockitoJUnitRunner.class)
public class InMemoryRestWithMockitoTest {

    public static interface BackendService {

        MyModel getMyModel(int number);
    }

    @Path("myresource")
    public static class MyResource {

        private BackendService backendService;

        @POST
        @Consumes(MediaType.TEXT_PLAIN)
        @Produces(MediaType.APPLICATION_XML)
        public MyModel createMyModel(int number) {

            return backendService.getMyModel(number);
        }

    }

    @InjectMocks
    public static MyResource sut = new MyResource();
    public static InMemoryRestServer server;

    @Mock
    private BackendService backendServiceMock;

    @BeforeClass
    public static void beforeClass() throws Exception {
        server = InMemoryRestServer.create(sut);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.close();
    }

    @Test
    public void postWithoutMocking() throws Exception {

        Response response = server.newRequest("/myresource").request().buildPost(Entity.text("42")).invoke();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void postWithMocking() throws Exception {

        when(backendServiceMock.getMyModel(42)).thenReturn(new MyModel(42));

        Response response = server.newRequest("/myresource").request().buildPost(Entity.text("42")).invoke();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        MyModel myModel = response.readEntity(MyModel.class);
        assertEquals(42, myModel.getResult());
    }
}
