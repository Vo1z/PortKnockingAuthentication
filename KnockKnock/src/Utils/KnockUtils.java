package Utils;

import java.util.*;

public class KnockUtils
{
    public static<T> T[] shuffleArray(T[] arrayToMix)
    {
        List<T> shuffledList = Arrays.asList(arrayToMix);
        Collections.shuffle(shuffledList);

        return (T[])shuffledList.toArray();
    }
}
