package za.co.twyst.tbxml.benchmark.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/** Ref. https://stackoverflow.com/questions/8481844/gridview-height-gets-cut?lq=1
 * 
 */
public class DataGrid extends GridView
       { // CONSTANTS
	
	     @SuppressWarnings("unused")
		 private static final String TAG = DataGrid.class.getSimpleName();
	     
	     // CONSTRUCTORS
	     
		 public DataGrid(Context context) 
		        { super(context);
		        }
	     
		 public DataGrid(Context context,AttributeSet attributes) 
		        { super(context,attributes);
		        }

		 public DataGrid(Context context,AttributeSet attributes, int style) 
		        { super(context,attributes,style);
		        }

		 // *** GridView ***

		 @Override
		 public void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
		        { // Calculate entire height by providing a very large height hint.
		          // View.MEASURED_SIZE_MASK represents the largest height possible.
		          
			 	  super.onMeasure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,MeasureSpec.AT_MOST));

			 	  ViewGroup.LayoutParams params = getLayoutParams();
		            
			 	  params.height = getMeasuredHeight();
			 	  
			 	  setLayoutParams(params);
		        }

       }
