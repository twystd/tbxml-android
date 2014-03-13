package za.co.twyst.tbxml.benchmark;

import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.Reader;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;

import za.co.twyst.tbxml.benchmark.Benchmark.PARSER;
import za.co.twyst.tbxml.benchmark.parsers.Parser;
import za.co.twyst.tbxml.benchmark.parsers.java.JAVA;
import za.co.twyst.tbxml.benchmark.parsers.jdk.DOM;
import za.co.twyst.tbxml.benchmark.parsers.jdk.XPATH;
import za.co.twyst.tbxml.benchmark.parsers.jdk.SAX;
import za.co.twyst.tbxml.benchmark.parsers.ndk.NDK;

import static za.co.twyst.tbxml.benchmark.Benchmark.PARSER.XPATH;
import static za.co.twyst.tbxml.benchmark.Benchmark.PARSER.DOM;
import static za.co.twyst.tbxml.benchmark.Benchmark.PARSER.SAX;
import static za.co.twyst.tbxml.benchmark.Benchmark.PARSER.TBXML;
import static za.co.twyst.tbxml.benchmark.Benchmark.PARSER.NDK;

public class MainActivity extends Activity
       { // CONSTANTS
	
	     private static final String   TAG        = MainActivity.class.getSimpleName();
	     private static final int      ITERATIONS = 10;
	     private static final PARSER[] PARSERS    = { XPATH,DOM,SAX,TBXML,NDK };
	     private static final int[]    WIDGETS    = { R.id.iterations,
	    	                                        R.id.info,
	    	 	                                    R.id.xpath,
	    	 	                                    R.id.dom,
	    	 	                                    R.id.sax,
	    	 	                                    R.id.java,
	    	 	                                    R.id.c
	                                              };
       
	     // INSTANCE VARIABLES
	     
	     private TextView         info;
         private EditText         iterations;
         private TextView         size;
	     private ProgressBar      windmill;
	     private View[]           widgets;
	     private GridView         grid;
	     private BenchmarkAdapter adapter;
	     private String           xml;
	     
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

                  windmill   = (ProgressBar) findViewById(R.id.windmill);
                  grid       = (GridView) findViewById(R.id.grid);
                  adapter    = new BenchmarkAdapter(getLayoutInflater(),PARSERS);
                  widgets    = new View[WIDGETS.length];
                  
                  for (int i=0; i<WIDGETS.length; i++)
                      { widgets[i] = findViewById(WIDGETS[i]);
                      }
                  
                  grid.setAdapter(adapter);

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
                  
                  adapter.clear();
                  
                  // ... initialise widgets
                  
                  iterations.setText(Integer.toString(ITERATIONS));
                  size.setText      ("Doc. Size: " + Integer.toString(xml.length()));
                }

         // INSTANCE METHODS
         
         public void onXPath(View v)
                { new ParseTask(XPATH,
                                new XPATH(xml),
                                loops()).execute();
                }
         
         public void onDOM(View v)
                { new ParseTask(DOM,
                                new DOM(xml),
                                loops()).execute();
                }
         
         public void onSax(View v)
                { new ParseTask(SAX,
                                new SAX(xml),
                                loops()).execute();
                }

         public void onTBXML(View v)
                { new ParseTask(TBXML,
                                new JAVA(xml),
                                loops()).execute();
                }

         public void onNDK(View v)
                { new ParseTask(NDK,
                                new NDK(xml),
                                loops()).execute();
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
                 { private final PARSER benchmark;
        	       private final String id;
        	       private final Parser parser;
        	       private final int    loops;
                 
                   public ParseTask(PARSER benchmark,
                                    Parser parser,
                                    int    loops)
                          { this.benchmark = benchmark;
                            this.id        = getString(benchmark.label);
                	        this.parser    = parser;
                	        this.loops     = loops;
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
       	                    
        	                    info.setText  (String.format("%s: %d ms",id,result.longValue()));
        	                    adapter.update(benchmark,result/loops);
        	                  }
                 }
       }
