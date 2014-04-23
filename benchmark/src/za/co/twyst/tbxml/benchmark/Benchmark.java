package za.co.twyst.tbxml.benchmark;

import java.util.ArrayList;
import java.util.List;

public class Benchmark 
       { // CONSTANTS
	
	     @SuppressWarnings("unused")
		 private static final String TAG = Benchmark.class.getSimpleName();
	     
	     public enum LABEL { XPATH(R.string.label_xpath),
	    	                 DOM  (R.string.label_dom),
	    	                 SAX  (R.string.label_sax),
	    	                 TBXML(R.string.label_tbxml),
	    	                 NDK  (R.string.label_ndk);
	     
	                         public final int label;
	                          
	                         private LABEL(int label)
	                                 { this.label = label;
	                                 }
	     	               };
	     
	     // INSTANCE VARIABLES
	     
	     public final LABEL parser;
	     
	     private List<Long> array = new ArrayList<Long>();
	     private Long       min;
	     private Long       max;
	     private Long       average;
	     private Integer    runs;

	     // CONSTRUCTOR
	     
	     public Benchmark(LABEL parser) 
	            { this.parser = parser;
	            
	              clear();
	            }
	     
	     // PROPERTIES
	     
	     public String min()
	            { return min == null ? "" : min.toString();
	            }
	     
	     public String max()
	            { return max == null ? "" : max.toString();
	            }
	     
	     public String average()
	            { return average == null ? "" : average.toString();
	            }
	     
	     public String runs()
	            { return runs == null ? "" : runs.toString();
	            }

	     // INSTANCE METHODS

	     public void clear() 
	            { this.array.clear();
	            
	              this.min     = null;
	              this.average = null;
	              this.max     = null;
	              this.runs    = null;
	            }
	     
		public void update(long result) 
		       { array.add(result);
 
                 long min = Long.MAX_VALUE;
                 long max = Long.MIN_VALUE;
                 long sum = 0;
       
		         for (Long value: array)
		             { sum += value;
		               min  = Math.min(min,value);
		               max  = Math.max(max,value);
		             }
            
		         this.min     = min;
		         this.average = sum/array.size();
		         this.max     = max;
		         this.runs    = array.size();
		       }
       }
