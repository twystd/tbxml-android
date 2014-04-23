package za.co.twyst.tbxml.benchmark.widgets;

import za.co.twyst.tbxml.benchmark.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Grid extends FrameLayout 
       { // CONSTANTS
	
	     @SuppressWarnings("unused")
		 private static final String TAG                = Grid.class.getSimpleName();
 		 private static final int    VERTICAL_SPACING   = 1;   // DP
	     private static final int    HORIZONTAL_SPACING = 1;   // DP

	     // INSTANCE VARIABLES
	     
	     private int rows    = 0;
	     private int columns = 0;
	     
	     private View[]       rowLabels    = new View[0];
	     private View[]       columnLabels = new View[0];
	     private View[][]     values       = new View[0][0];
	     private TextView[][] valuesx      = new TextView[0][0];
	     
	     private int verticalSpacing   = 1;
	     private int horizontalSpacing = 1;

	     // CLASS METHODS
	 	
	     private static int dpToPx(Context context,int dp) 
	             { return Math.round(dp * (context.getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
	             }

	     // CONSTRUCTORS

	 	 public Grid(Context context) 
	 	        { super(context);

	              initialise(context,null);
	 	        }
	
	 	 public Grid(Context context,AttributeSet attributes) 
	 	        { super(context,attributes);

	              initialise(context,attributes);
	 	        }

	 	 public Grid(Context context,AttributeSet attributes,int style) 
	 	        { super(context,attributes,style);

	              initialise(context,attributes);
	 	        }

	 	 // PROPERTIES
	 	 
	     public void setRowLabels(int[] rows,LayoutInflater inflater,int layout,int tv) 
			    { rowLabels = new TextView[rows.length];
			    
			      for (int i=0; i<rows.length; i++)
			          { View     label    = inflater.inflate(layout,this,false);
			            TextView textview = (TextView) label.findViewById(tv);
			          
			            textview.setText(rows[i]);
			            
			            rowLabels[i] = label;
			          }
			      
	              for (View label: rowLabels)
	                  { addView(label);
	                  }
			    }
	 	 
	     public void setColumnLabels(int[] columns,LayoutInflater inflater,int layout,int tv) 
			    { this.columnLabels = new TextView[columns.length];
			    
			      for (int i=0; i<columns.length; i++)
			          { View     label    = inflater.inflate(layout,this,false);
			            TextView textview = (TextView) label.findViewById(tv);
			          
			            textview.setText(columns[i]);
			            
			            columnLabels[i] = label;
			          }
			      
	              for (View label: columnLabels)
	                  { addView(label);
	                  }
			    }

	     public void setValues(int rows,int cols,LayoutInflater inflater,int layout,int tv) 
		        { this.rows    = rows;
	    	      this.columns = cols;
	    	      this.values  = new View[rows][cols];
		          this.valuesx = new TextView[rows][cols];
			    
		          for (int i=0; i<rows; i++)
		              { for (int j=0; j<cols; j++)
		                    { View     value    = inflater.inflate(layout,this,false);
		    	              TextView textview = (TextView) value.findViewById(tv);
		          
		                      values[i][j]  = value;
		                      valuesx[i][j] = textview;
		                    }
		              }
		      
		          for (int i=0; i<rows; i++)
    	              { for (int j=0; j<cols; j++)
    	                    { addView(values[i][j]);
    	                    }
    	              }
		        }

	     public void setValue(int row,int col,String text)
	            { valuesx[row][col].setText(text);
	            }

	     // IMPLEMENTATION
	     
	     private void initialise(Context context,AttributeSet attributes) 
	             { // ... set default style values
		
	    	       verticalSpacing   = dpToPx(context,VERTICAL_SPACING);
	    	       horizontalSpacing = dpToPx(context,HORIZONTAL_SPACING);

	    	       // ... parse attributes
		
	    	       if (attributes != null) 
	    	          { parse(context,attributes);
	    	          }
	             }

	     private void parse(Context context,AttributeSet attributes) 
	             { TypedArray array = context.obtainStyledAttributes(attributes,R.styleable.Grid);
	               int        N     = array.getIndexCount();
		
	               for (int i=0; i<N; ++i)
	                   { int attr = array.getIndex(i);
			   
	                     switch (attr)
	                            { case R.styleable.Grid_verticalSpacing:
	                            	   this.verticalSpacing = (int) array.getDimension(attr,this.verticalSpacing);
	                            	   break;

	                              case R.styleable.Grid_horizontalSpacing:
	                            	   this.horizontalSpacing = (int) array.getDimension(attr,this.horizontalSpacing);
	                            	   break;
	                            }
	                   }	

	               array.recycle();
	             }

	     // *** FrameLayout ***
	 	 
	 	 @Override
	 	 protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) 
	 	           { super.onMeasure(widthMeasureSpec,heightMeasureSpec);
	 	           
	 	             int width  = getMeasuredWidth();
	 	             
	 	             if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY)
	 	                { int h      = 0;
	 	            	  int insetx = 0;
	 	                  int insety = 0;

	 	                  // ... measure column label row height
	 	                  
	 	                  for (View label: columnLabels)
 	                          { label.measure(MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED),
 	                    		              MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
 	                      
 	                	         insety = Math.max(insety,label.getMeasuredHeight());
 	                          }
	 	                  
	 	                  insety += verticalSpacing;
	 	                  
	 	                  // ... measure row label column width
	 	                  
	 	                  LayoutParams params;
	 	                  
	 	                  for (View label: rowLabels)
	 	                      { label.measure(MeasureSpec.makeMeasureSpec(width,MeasureSpec.UNSPECIFIED),
	 	                    		          MeasureSpec.makeMeasureSpec(0,    MeasureSpec.UNSPECIFIED));
	 	                  
	 	                        params = (LayoutParams) label.getLayoutParams();
	 	                	    insetx  = Math.max(insetx,label.getMeasuredWidth() + params.leftMargin + params.rightMargin);
	 	                	    h      += label.getMeasuredHeight();
	 	                      }

	 	                  // ... re-measure column labels
	 	                  
	 	                  int rowHeight = h/values.length;
	 	                  int gridWidth = (width - getPaddingLeft() - getPaddingRight() - insetx);
	 	                  int colwidth  = (gridWidth - (columns - 1)*horizontalSpacing)/columns;
	 	                  
	 	                  for (View label: columnLabels)
	                          { label.measure(MeasureSpec.makeMeasureSpec(colwidth,                 MeasureSpec.EXACTLY),
	                    		              MeasureSpec.makeMeasureSpec(label.getMeasuredHeight(),MeasureSpec.EXACTLY));
	                          }

	 	                  // ... re-measure values
	 	                  
	 	 		          for (int i=0; i<values.length; i++)
     	 		              { View[] row = values[i];
	 		                    View   value;
	 		              
	 		                    for (int j=0; j<row.length; j++)
	 		                        { value = values[i][j];
	 		                        
	 		                          value.measure(MeasureSpec.makeMeasureSpec(colwidth, MeasureSpec.EXACTLY),
	 		                        		        MeasureSpec.makeMeasureSpec(rowHeight,MeasureSpec.EXACTLY));
	 		                        }
     	 		             }

	 	                  // ... update measured size
	 	                  
	 	                  h += getPaddingTop();
	 	                  h += getPaddingBottom();
	 	 		          h += insety;
	 	 		          h += (rows-1) * verticalSpacing;

	 	                  if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST)
		 	                 setMeasuredDimension(width,Math.min(h,MeasureSpec.getSize(heightMeasureSpec)));
	 	                     else
	 	                     setMeasuredDimension(width,h);
	 	                }
	 	           }

	 	 @Override
	 	 protected void onLayout(boolean changed,int left,int top,int right,int bottom) 
	 	           { int insetx = 0;
	 	             int insety = 0;

	 	             for (View label: rowLabels)
	 	                 { LayoutParams params = (LayoutParams) label.getLayoutParams();
	 	                 
	 	                   insetx = Math.max(insetx,label.getMeasuredWidth() + params.leftMargin + params.rightMargin);
	 	                 }

	 	             for (View label: columnLabels)
 	                     { insety = Math.max(insety,label.getMeasuredHeight());
 	                     }

	 	             int x = getPaddingLeft() + insetx;
	 	             int y = getPaddingTop () + insety + verticalSpacing;
	 	             int h;
	 	            
	 		         for (View label: columnLabels)
                         { label.layout(x,
                        		        getPaddingTop(),
                        		        x + label.getMeasuredWidth(),
                        		        getPaddingTop() + label.getMeasuredHeight());
                     
                            x += label.getMeasuredWidth() + horizontalSpacing;
                         }
                   
	 		         for (View label: rowLabels)
                         { label.layout(getPaddingLeft(),
                        		        y,
                        		        getPaddingLeft() + label.getMeasuredWidth(),
                        		        y + label.getMeasuredHeight());
                         
                           y += label.getMeasuredHeight() + verticalSpacing;
                         }
	 		         
	 		         // ... layout value grid
	 		         
	 		         y = getPaddingTop () + insety + verticalSpacing;
	 		         
	 		         for (int i=0; i<values.length; i++)
	 		             { View[] row = values[i];
	 		               View   value;
	 		             
	 		               x = getPaddingLeft() + insetx;
	 		               h = 0;
	 		               
	 		               for (int j=0; j<row.length; j++)
	 		                   { value = values[i][j];
	 		                     h     = Math.max(h, value.getMeasuredHeight());
	 		                     
	 		                     value.layout(x,
                      		                  y,
                      		                  x + value.getMeasuredWidth (),
                      		                  y + value.getMeasuredHeight());
	 		                     
	 		                     x += value.getMeasuredWidth() + horizontalSpacing;
	 		                   }
	 		               
	 		               y += h + verticalSpacing;
	 		             }
	 	           }
       }
