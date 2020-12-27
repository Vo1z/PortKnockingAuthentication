import Utils.KnockUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class KnockUtilsTest
{
    @Test
    public void shuffleArray()
    {
        String[] array = new String[]{"a", "b", "c", "d"};
        String[] arrayToShuffle = Arrays.copyOf(array, array.length);
        KnockUtils.shuffleArray(arrayToShuffle);

        Assert.assertNotEquals(array, arrayToShuffle);
    }
}