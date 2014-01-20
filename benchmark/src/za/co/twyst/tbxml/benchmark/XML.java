package za.co.twyst.tbxml.benchmark;

import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;

import za.co.twyst.tbxml.benchmark.parsers.Parser;
import za.co.twyst.tbxml.benchmark.parsers.java.JAVA;
import za.co.twyst.tbxml.benchmark.parsers.jdk.JDK;
import za.co.twyst.tbxml.benchmark.parsers.ndk.NDK;

public class XML extends Activity
       { // CONSTANTS
	
	     private static final String TAG        = XML.class.getSimpleName();
	     private static final int    ITERATIONS = 10;
       
	     // INSTANCE VARIABLES
	     
	     private TextView    info;
         private EditText    iterations;
         private TextView    size;
	     private Button      jdk;
	     private Button      java;
	     private Button      c;
	     private ProgressBar windmill;

	     private TextView   jdk_min;
         private TextView   jdk_ave;
         private TextView   jdk_max;
         private TextView   jdk_runs;

         private TextView   java_min;
         private TextView   java_ave;
         private TextView   java_max;
         private TextView   java_runs;

         private TextView   c_min;
         private TextView   c_ave;
         private TextView   c_max;
         private TextView   c_runs;

	     private String     xml;
	     private List<Long> jdkx  = new ArrayList<Long>();
         private List<Long> javax = new ArrayList<Long>();
         private List<Long> cx    = new ArrayList<Long>();
	     
	     // CLASS METHODS
	     
	     private static void close(Closeable stream)
	             { try
	                  { stream.close();
	                  }
	               catch(Throwable x)
	                  { Log.w(TAG,"Error closing stream",x);
	                  }
	             }
	     
         // *** Activity ***

         @Override
         public void onCreate(Bundle savedInstanceState)
                { super.onCreate(savedInstanceState);

                  setContentView(R.layout.tbxml);
                  
                  info       = (TextView) findViewById(R.id.info);
                  iterations = (EditText) findViewById(R.id.iterations);
                  size       = (TextView) findViewById(R.id.size);

                  jdk       = (Button)      findViewById(R.id.jdk);
                  java      = (Button)      findViewById(R.id.java);
                  c         = (Button)      findViewById(R.id.c);
                  windmill  = (ProgressBar) findViewById(R.id.windmill);

                  jdk_min   = (TextView) findViewById(R.id.jdk_min);
                  jdk_ave   = (TextView) findViewById(R.id.jdk_ave);
                  jdk_max   = (TextView) findViewById(R.id.jdk_max);
                  jdk_runs  = (TextView) findViewById(R.id.jdk_runs);

                  java_min  = (TextView) findViewById(R.id.java_min);
                  java_ave  = (TextView) findViewById(R.id.java_ave);
                  java_max  = (TextView) findViewById(R.id.java_max);
                  java_runs = (TextView) findViewById(R.id.java_runs);

                  c_min  = (TextView) findViewById(R.id.c_min);
                  c_ave  = (TextView) findViewById(R.id.c_ave);
                  c_max  = (TextView) findViewById(R.id.c_max);
                  c_runs = (TextView) findViewById(R.id.c_runs);

                  // ... preload XML
                  
                  Reader in = new InputStreamReader(getResources().openRawResource(R.raw.routes));
                  
                  try
                      { StringBuilder string = new StringBuilder();
                        char[]        buffer = new char[4096];
                        int           N;
                  
                        while ((N = in.read(buffer)) != -1)
                              { string.append(buffer,0,N);
                              }

                        xml = string.toString();
                      }
                  catch(Throwable x)
                      { Log.w(TAG, "Error reading XML file",x);
                      }
                  finally
                     { close(in);
                     }

                  // ... clear measurement arrays
                  
                  jdkx.clear ();
                  javax.clear();
                  cx.clear   ();
                  
                  // ... initialise widgets
                  
                  iterations.setText(Integer.toString(ITERATIONS));
                  size.setText      ("Doc. Size: " + Integer.toString(xml.length()));
                }

         // INSTANCE METHODS
         
         public void onJDK(View v)
                { new ParseTask("JDK",
                                jdkx,
                                jdk_min,
                                jdk_ave,
                                jdk_max,
                                jdk_runs,
                                new JDK(xml),
                                loops()).execute();
                }

         public void onJAVA(View v)
                { new ParseTask("Java",
                                javax,
                                java_min,
                                java_ave,
                                java_max,
                                java_runs,
                                new JAVA(xml),
                                loops()).execute();
                }

         public void onC(View v)
                { new ParseTask("Native",
                                cx,
                                c_min,
                                c_ave,
                                c_max,
                                c_runs,
                                new NDK(xml),
                                loops()).execute();
                }
         
         // IMPLEMENTATION
         
         private void parsed(String parser,long dt)
                 { info.setText(String.format("%s: %d ms",parser,dt));
                 }

         private void error(String parser,String message)
                 { Toast.makeText(this,parser + "::" + message,Toast.LENGTH_SHORT).show();
                 }
         
         private int loops()
                 { try
                      { return Integer.parseInt(iterations.getText().toString());
                      }
                   catch(Throwable x)
                      { // IGNORE
                      }
                 
                   return ITERATIONS;
                 }
         
         // INNER CLASSES
         
         private class ParseTask extends AsyncTask<Void,Void,Long>
                 { private final String     id;
                   private final List<Long> array;
                   private final TextView   min;
                   private final TextView   ave;
                   private final TextView   max;
                   private final TextView   runs;
        	       private final Parser     parser;
        	       private final int        loops;
                 
                   public ParseTask(String id,
                                    List<Long> array,
                                    TextView min,
                                    TextView ave,
                                    TextView max,
                                    TextView runs,
                                    Parser   parser,
                                    int      loops)
                          { this.id     = id;
                            this.array  = array;
                            this.min    = min;
                            this.ave    = ave;
                            this.max    = max;
                            this.runs   = runs;
                	        this.parser = parser;
                	        this.loops  = loops;
                          }

        	       @Override
        	       protected Long doInBackground(Void... params) 
        	                 { try
        	                      { return parser.parse(loops); 
        	                      }
        	                   catch(Throwable x)
        	                      { Log.w(TAG,"Error in parser " + id,x);
        	                      }
        	                 
        	    	           return null;
        	                 }
        	       
        	       @Override
        	       protected void onPreExecute() 
        	                 { jdk.setEnabled       (false);
        	                   java.setEnabled      (false);
        	                   c.setEnabled         (false);
        	                   iterations.setEnabled(false);
        	                   
        	                   windmill.setVisibility(View.VISIBLE);
        	                 }

        	       @Override
       			   protected void onPostExecute(Long result) 
        	                 { windmill.setVisibility(View.GONE);

        	                   jdk.setEnabled       (true);
                               java.setEnabled      (true);
                               c.setEnabled         (true);
                               iterations.setEnabled(true);
                             
        	                   if (result == null)
        	                      { error(id,"ERROR");
        	                 	    return;
        	                      }
        	                  
        	                    parsed(id,result.longValue());
        	                    
        	                    // ... update statistics
        	                    
        	                    long _min = Long.MAX_VALUE;
        	                    long _max = Long.MIN_VALUE;
        	                    long _sum = 0;
        	                    
        	                    array.add(result/loops);
        	                    
        	                    for (Long value: array)
        	                        { _sum += value;
        	                          _min  = Math.min(_min,value);
                                      _max  = Math.max(_max,value);
        	                        }
        	                    
        	                    min.setText (Long.toString(_min));
                                ave.setText (Long.toString(_sum/array.size()));
                                max.setText (Long.toString(_max));
                                runs.setText(Integer.toString(array.size()));
        	                  }
                 }
       }
