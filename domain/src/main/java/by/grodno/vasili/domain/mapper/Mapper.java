package by.grodno.vasili.domain.mapper;


import java.util.ArrayList;
import java.util.List;

/**
 * Base mapper for conversion objects
 *
 * @param <From> type for conversion
 * @param <To>   type to conversion
 */
@SuppressWarnings("WeakerAccess")
public abstract class Mapper<From, To> {

    /**
     * Direct conversion for one object
     *
     * @param from source for conversion
     * @return converted object
     */
    public abstract To map(From from);

    /**
     * Reverse conversion. Need to override if necessary
     *
     * @param to source for conversion
     * @return converted object
     */
    public From reverseMap(To to) {
        throw new AbstractMethodError("Not implemented method");
    }

    /**
     * Direct conversion for list of objects
     *
     * @param fromList source list of objects
     * @return converted list of objects
     */
    public List<To> mapList(List<From> fromList) {
        List<To> toList = new ArrayList<>(fromList.size());
        for (From from : fromList) {
            if (from == null) continue;
            toList.add(map(from));
        }
        return toList;
    }

    /**
     * Reverse conversion for list of objects
     *
     * @param toList source list of objects
     * @return converted list of objects
     */
    public List<From> reverseMapList(List<To> toList) {
        List<From> fromList = new ArrayList<>(toList.size());
        for (To to : toList) {
            if (to == null) continue;
            fromList.add(reverseMap(to));
        }
        return fromList;
    }
}
