import Server.AuthenticationServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

public class AuthenticationServerTest
{
    private final int numberOfOpenedSockets = 10;
    private final AuthenticationServer server = new AuthenticationServer(this.numberOfOpenedSockets);

    @Before @Test
    public void isWorkingBefore()
    {
        this.server.startServer();
        Assert.assertTrue(this.server.isWorking());
    }

    @After @Test
    public void isWorkingAfter()
    {
        this.server.stopServer();
        Assert.assertFalse(this.server.isWorking());
    }

    @Test
    public void startServer() throws InterruptedException
    {
        int[] ports = Arrays.stream(this.server.getAuthenticationPorts())
                .distinct()
                .toArray();

        Assert.assertEquals(this.server.getAuthenticationPorts().length, this.numberOfOpenedSockets);
        Assert.assertArrayEquals(ports, this.server.getAuthenticationPorts());
    }

    @Test
    public void stopServer()
    {
        this.server.stopServer();

        Assert.assertNull(this.server.getAuthenticationPorts());
        Assert.assertFalse(this.server.isWorking());
    }

    @Test public void openSocketForSuchAddress()
    {
        Assert.fail();
    }

    @Test
    public void checkAuthentication()
    {
        this.server.addAddressToAuthenticationList("1", 0);
        this.server.addAddressToAuthenticationList("1", 1);
        this.server.addAddressToAuthenticationList("1", 2);

        Assert.assertTrue(this.server.checkAuthentication("1", 3));
        Assert.assertFalse(this.server.checkAuthentication("1", 4));
    }

    @Test
    public void addAddressToAuthenticationList()
    {
        this.server.addAddressToAuthenticationList("1", 0);
        this.server.addAddressToAuthenticationList("1", 1);
        this.server.addAddressToAuthenticationList("1", 2);

        Assert.assertTrue(this.server.checkAuthentication("1",3));
    }

    @Test
    public void removeAddressFromAuthenticationList()
    {
        String addressToRemove = "remove";
        String addressToKeep = "keep";

        this.server.addAddressToAuthenticationList(addressToKeep, 0);
        this.server.addAddressToAuthenticationList(addressToKeep, 1);
        this.server.addAddressToAuthenticationList(addressToRemove, 2);
        this.server.addAddressToAuthenticationList(addressToKeep, 2);
        this.server.addAddressToAuthenticationList(addressToKeep, 3);
        this.server.addAddressToAuthenticationList(addressToRemove, 4);

        this.server.removeAddressFromAuthenticationList(addressToRemove, 4);

        Assert.assertEquals(0, Arrays.stream(this.server.getAuthenticationAddresses())
                .filter(set -> set.contains(addressToRemove))
                .count());
    }

    @Test
    public void getAuthenticationPorts()
    {
        int[] ports = this.server.getAuthenticationPorts();

        Assert.assertEquals(ports.length, Arrays.stream(ports)
                .distinct()
                .count());
    }

    @Test
    public void getAuthenticationAddresses()
    {
        String address = "addressForReal";
        String anotherAddress = "anotherGuy";

        for(int i = 0; i < this.numberOfOpenedSockets; i++)
        {
            this.server.addAddressToAuthenticationList(address, i);
            if (i % 2 == 0)
                this.server.addAddressToAuthenticationList(anotherAddress, i);
        }

        Set<String>[] set = this.server.getAuthenticationAddresses();

        Assert.assertEquals(this.numberOfOpenedSockets, set.length);
        for(int i = 0; i < this.numberOfOpenedSockets; i++)
        {
            if (i % 2 == 0)
                Assert.assertTrue(set[i].contains(address) && set[i].contains(anotherAddress) && set[i].size() == 2);
            else
                Assert.assertTrue(set[i].contains(address) && set[i].size() == 1);
        }
    }
}