package ngordnet;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T>  {   
    /** Constructs a new empty TimeSeries. */
    public TimeSeries()  {
    }

    /** Returns the years in which this time series is valid. Doesn't really
      * need to be a NavigableSet. This is a private method and you don't have 
      * to implement it if  you don't want to. */
    // private NavigableSet<Integer> validYears(int startYear, int endYear) {

    // }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR. 
     * inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear)  {
        for (int ctr = startYear; ctr <= endYear; ctr += 1)  {
            this.put(ctr, ts.get(ctr));
        }
    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts)  {
        this.putAll(ts);
    }

    /** Returns the quotient of this time series divided by the relevant value in ts.
      * If  ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts)  {
        TimeSeries<Double> divide = new TimeSeries<Double>();
        List<Integer> keys = new ArrayList();
        keys.addAll(this.keySet());
        for (Integer key : keys)  {
            if (!ts.containsKey(key))  {
                throw new IllegalArgumentException();
            }
            Double value = (this.get(key)).doubleValue() / (ts.get(key)).doubleValue();
            divide.put(key, value);
        }
        return divide;
    }

    /** Returns the sum of this time series with the given ts. The result is a 
      * a Double time series (for  simplicity). */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts)  {
        TimeSeries<Double> plus = new TimeSeries<Double>();
        // Finding the dif ference in key sets
        Set<Integer> keyUnion = new HashSet<Integer>(this.keySet());
        keyUnion.addAll(ts.keySet());
        Set<Integer> intersection = new HashSet<Integer>(this.keySet());
        intersection.retainAll(ts.keySet());
        keyUnion.removeAll(intersection);
        // keyUnion now contains the set dif ference of the keySets
        List<Integer> normalKeys = new ArrayList();
        normalKeys.addAll(intersection);
        for (Integer key : normalKeys) {
            Double value = (this.get(key)).doubleValue() + (ts.get(key)).doubleValue();
            plus.put(key, value);
        }
        List<Integer> specialKeys = new ArrayList();
        specialKeys.addAll(keyUnion);
        for (Integer key : specialKeys)  {
            if (!this.containsKey(key))  {
                plus.put(key, (ts.get(key).doubleValue()));
            } else {
                plus.put(key, (this.get(key).doubleValue()));
            }
        }
        return plus;
    }

      /** Returns all years for  this time series (in any order). */
    public Collection<Number> years()  {
        Set<Number> years = new TreeSet<Number>();
        for (Integer value: this.keySet())  {
            years.add(value);
        }
        return years;
    }

      /** Returns all data for  this time series. 
        * Must be in the same order as years(). */
    public Collection<Number> data()  {
        Collection<Number> valuesCol = new TreeSet<Number>();
        for (Integer value: this.keySet())  {
            valuesCol.add(this.get(value));
        }
        return valuesCol;
    } 
}
