import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class Misc {
    /**
     * gets and returns the individual distances between a short and a set of shorts.
     * @param kr: the short used to measure distances to a set of other shorts.
     * @param keySet: the other shorts used to measure distances to a specified short.
     * @return: the list of distances.
     */
    List<Integer> getDistances(Short kr, Set<Short> keySet)
    {
        List<Integer> distances = new ArrayList<>();

        keySet.remove(kr);

        keySet.forEach((key) -> {
            distances.add(kr - key);
        });

        return distances;
    }
}
