package za.co.twyst.tbxml.benchmark;

import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.Reader;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;

import za.co.twyst.tbxml.benchmark.Benchmark.LABEL;
import za.co.twyst.tbxml.benchmark.parsers.Parser;
import za.co.twyst.tbxml.benchmark.parsers.java.JAVA;
import za.co.twyst.tbxml.benchmark.parsers.jdk.DOM;
import za.co.twyst.tbxml.benchmark.parsers.jdk.XPATH;
import za.co.twyst.tbxml.benchmark.parsers.jdk.SAX;
import za.co.twyst.tbxml.benchmark.parsers.ndk.NDK;
import za.co.twyst.tbxml.benchmark.widgets.Grid;

import static za.co.twyst.tbxml.benchmark.Benchmark.LABEL.XPATH;
import static za.co.twyst.tbxml.benchmark.Benchmark.LABEL.DOM;
import static za.co.twyst.tbxml.benchmark.Benchmark.LABEL.SAX;
import static za.co.twyst.tbxml.benchmark.Benchmark.LABEL.TBXML;
import static za.co.twyst.tbxml.benchmark.Benchmark.LABEL.NDK;

public class MainActivity extends Activity
       { // CONSTANTS
	
	     private static final String  TAG        = MainActivity.class.getSimpleName();
	     private static final int     ITERATIONS = 10;
	     private static final LABEL[] PARSERS    = { XPATH,DOM,SAX,TBXML,NDK };
	     private static final int[]   ROWS       = { XPATH.label,DOM.label,SAX.label,TBXML.label,NDK.label    };
	     private static final int[]   COLUMNS    = { R.string.min,R.string.average,R.string.max,R.string.runs };
	     private static final int[]   WIDGETS    = { R.id.iterations,
	    	                                         R.id.info,
	    	 	                                     R.id.xpath,
	    	 	                                     R.id.dom,
	    	 	                                     R.id.sax,
	    	 	                                     R.id.java,
	    	 	                                     R.id.c
	                                               };

	     // INSTANCE VARIABLES
	     
	     private TextView    info;
         private EditText    iterations;
         private TextView    size;
	     private ProgressBar windmill;
	     private View[]      widgets;
	     private Grid        gridx;
	     private String      xml;
	     private Benchmark[] benchmarks;

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
                  
                  // ... initialise
                  
	              benchmarks = new Benchmark[PARSERS.length];
	              
	              for (int i=0; i<PARSERS.length; i++)
	                  { benchmarks[i] = new Benchmark(PARSERS[i]);
	                  }

	              // ... initialise widgets
	              
                  info       = (TextView) findViewById(R.id.info);
                  iterations = (EditText) findViewById(R.id.iterations);
                  size       = (TextView) findViewById(R.id.size);

                  windmill   = (ProgressBar) findViewById(R.id.windmill);
                  gridx      = (Grid) findViewById(R.id.gridx);
                  widgets    = new View[WIDGETS.length];
                  
                  for (int i=0; i<WIDGETS.length; i++)
                      { widgets[i] = findViewById(WIDGETS[i]);
                      }
                  
                  // ... initialise grid
                  
                  gridx.setRowLabels   (ROWS,   getLayoutInflater(),R.layout.label,R.id.textview);
                  gridx.setColumnLabels(COLUMNS,getLayoutInflater(),R.layout.value,R.id.textview);
                  gridx.setValues      (ROWS.length,COLUMNS.length,getLayoutInflater(),R.layout.value,R.id.textview);
                  
                  // ... preload XML
                  
                  Reader in = new InputStreamReader(getResources().openRawResource(R.raw.db));
                  
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

                  // ... initialise widgets
                  
                  iterations.setText(Integer.toString(ITERATIONS));
                  size.setText      ("Doc. Size: " + Integer.toString(xml.length()));
                }

         // INSTANCE METHODS
         
         public void onXPath(View v)
                { new ParseTask(getString(XPATH.label),
                                new XPATH(xml),
                                loops(),
                                0).execute();
                }
         
         public void onDOM(View v)
                { new ParseTask(getString(DOM.label),
                                new DOM(xml),
                                loops(),
                                1).execute();
                }
         
         public void onSax(View v)
                { new ParseTask(getString(SAX.label),
                                new SAX(xml),
                                loops(),
                                2).execute();
                }

         public void onTBXML(View v)
                { new ParseTask(getString(TBXML.label),
                                new JAVA(xml),
                                loops(),
                                3).execute();
                }

         public void onNDK(View v)
                { new ParseTask(getString(NDK.label),
                                new NDK(xml),
                                loops(),
                                4).execute();
                }
         
         // IMPLEMENTATION
         
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
                 { private final String id;
        	       private final Parser parser;
        	       private final int    loops;
        	       private final int    row;
                 
                   public ParseTask(String label,
                		            Parser parser,
                                    int    loops,
                                    int    row)
                          { this.id     = label;
                	        this.parser = parser;
                	        this.loops  = loops;
                	        this.row    = row;
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
        	                 { for (View widget: widgets)
        	                	   { widget.setEnabled(false);
        	                	   }
        	                   
        	                   windmill.setVisibility(View.VISIBLE);
        	                 }

        	       @Override
       			   protected void onPostExecute(Long result) 
        	                 { // ... re-enable UI
        	    	   
        	    	           windmill.setVisibility(View.GONE);

        	                   for (View widget: widgets)
        	                       { widget.setEnabled(true);
        	                       }
                             
        	                   if (result == null)
        	                      { error(id,"ERROR");
        	                 	    return;
        	                      }

        	                    // ... update statistics
       	                    
        	                    Benchmark benchmark = benchmarks[row];
        	                    
        	                    info.setText(String.format("%s: %d ms",id,result.longValue()));
        	                    
        	                    benchmark.update(result/loops);
        	                    
        	                    gridx.setValue(row,0,benchmark.min());
        	                    gridx.setValue(row,1,benchmark.average());
   	                         	gridx.setValue(row,2,benchmark.max());
   	                         	gridx.setValue(row,3,benchmark.runs());
        	                  }
                 }
       }
