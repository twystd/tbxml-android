package za.co.twyst.tbxml.benchmark.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import za.co.twyst.tbxml.benchmark.R;

public class GroupBox extends FrameLayout 
       { // CONSTANTS

 		 private static final int LABEL_TEXT_SIZE     = 14;
	     private static final int LABEL_MARGIN_LEFT   = 24;
	     private static final int LABEL_MARGIN_RIGHT  = 24;
	     private static final int LABEL_PADDING_LEFT  = 6;
	     private static final int LABEL_PADDING_RIGHT = 4;
	     private static final int CORNER_RADIUS       = 8;
	     private static final int STROKE_WIDTH        = 1;

	     // ATTRIBUTE VARIABLES

	     private String label             = "";
	     private int    labelColour       = Color.argb(0x00ff,0x44,0x44,0x44);
	     private float  labelTextSize     = 20;
	     private float  labelMarginLeft   = 24;
	     private float  labelMarginRight  = 24;
	     private float  labelPaddingLeft  = 6;
	     private float  labelPaddingRight = 4;
	     private float  cornerRadius      = 8;
	     private float  strokeWidth       = 1;
	     private int    strokeColour      = Color.argb(0x00ff,0x44,0x44,0x44);
	
	     // INSTANCE VARIABLES

	     private StaticLayout text;
	     private Paint        paint;
	     private Paint        paintx;
	     private Path         path;
	     private PathEffect   effect;

	     private float textWidth  = 0;
	     private float textMiddle = 0;

	     // CLASS METHODS
	
	     private static int dpToPx(Context context,int dp) 
	             { return Math.round(dp * (context.getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
	             }

	     // CONSTRUCTORS
	     
	     public GroupBox(Context context) 
	            { super(context);
	
	              initialise(context,null);
	            }

	     public GroupBox(Context context,AttributeSet attributes) 
	            { super(context,attributes);

	              initialise(context,attributes);
	            }

	     public GroupBox(Context context, AttributeSet attributes,int defstyle) 
	            { super(context,attributes,defstyle);

	              initialise(context,attributes);
	            }

	     // IMPLEMENTATION
	     
	     private void initialise(Context context,AttributeSet attributes) 
	             { // ... set default style values
		
	    	       labelTextSize     = dpToPx(context,LABEL_TEXT_SIZE);
	    	       labelMarginLeft   = dpToPx(context,LABEL_MARGIN_LEFT);
	    	       labelMarginRight  = dpToPx(context,LABEL_MARGIN_RIGHT);
	    	       labelPaddingLeft  = dpToPx(context,LABEL_PADDING_LEFT);
	    	       labelPaddingRight = dpToPx(context,LABEL_PADDING_RIGHT);
	    	       cornerRadius      = dpToPx(context,CORNER_RADIUS);
	    	       strokeWidth       = dpToPx(context,STROKE_WIDTH);

	    	       // ... parse attributes
		
	    	       if (attributes != null) 
	    	          { parse(context,attributes);
	    	          }
		
	    	       // ... initialise paint and path
		
	    	       effect = new CornerPathEffect(cornerRadius);
	    	       path   = new Path();
	    	       paint  = new Paint();
	    	       paintx = new Paint();
	    	       
	    	       paint.setColor      (strokeColour);
	    	       paint.setStyle      (Paint.Style.STROKE);
	    	       paint.setStrokeWidth(strokeWidth);
	    	       paint.setAntiAlias  (true);
	    	       paint.setDither     (true);
	    	       paint.setPathEffect (effect);
	    	       paint.setStrokeJoin (Paint.Join.BEVEL);  
	    	       paint.setStrokeCap  (Paint.Cap.BUTT);  

	    	       paintx.setColor    (labelColour);
	    	       paintx.setTextSize (labelTextSize);
	    	       paintx.setTypeface (Typeface.SERIF);
	    	       paintx.setTextAlign(Align.LEFT);

	    	       Rect bounds = new Rect();

	    	       paintx.getTextBounds ("a",0,1,bounds);
	    	       
	    	       this.textMiddle = bounds.height()/2;
	    	       this.textWidth = StaticLayout.getDesiredWidth(label,new TextPaint(paintx));
	    	       this.text      = new StaticLayout(label,
	    	    		                             0,
	    	    		                             label.length(),
	    	    		                             new TextPaint(paintx),
	    	    		                             10000,
	    	    		                             Layout.Alignment.ALIGN_NORMAL,
	    	    		                             1,
	    	    		                             0,
	    	    		                             true,
	    	    		                             TextUtils.TruncateAt.END,
	    	    		                             (int) textWidth);
	             }
	
	     private void parse(Context context,AttributeSet attributes) 
	             { TypedArray array = context.obtainStyledAttributes(attributes,R.styleable.GroupBox);
	               int        N     = array.getIndexCount();
		
	               for (int i=0; i<N; ++i)
	                   { int attr = array.getIndex(i);
			   
	                     switch (attr)
	                            { case R.styleable.GroupBox_label:
	                            	   this.label = array.getString(attr);
	                            	   break;
			                
	                              case R.styleable.GroupBox_labelColour:
	                            	   this.labelColour = array.getColor(attr,this.labelColour);
	                            	   break;

	                              case R.styleable.GroupBox_labelTextSize:
	                            	   this.labelTextSize = array.getDimension(attr,this.labelTextSize);
	                            	   break;

	                              case R.styleable.GroupBox_labelMarginLeft:
	                            	   this.labelMarginLeft = array.getDimension(attr,this.labelMarginLeft);
	                            	   break;

	                              case R.styleable.GroupBox_labelMarginRight:
	                            	   this.labelMarginRight = array.getDimension(attr,this.labelMarginRight);
	                            	   break;

	                              case R.styleable.GroupBox_labelPaddingLeft:
	                            	   this.labelPaddingLeft = array.getDimension(attr,this.labelPaddingLeft);
	                            	   break;

	                              case R.styleable.GroupBox_labelPaddingRight:
	                            	   this.labelPaddingRight = array.getDimension(attr,this.labelPaddingRight);
	                            	   break;

	                              case R.styleable.GroupBox_cornerRadius:
	                                   this.cornerRadius = array.getDimension(attr,this.cornerRadius);
	                                   break;

	                              case R.styleable.GroupBox_strokeWidth:
	                            	   this.strokeWidth = array.getDimension(attr,this.strokeWidth);
	                            	   break;

	                              case R.styleable.GroupBox_strokeColour:
	                            	   this.strokeColour = array.getColor(attr,this.strokeColour);
	                            	   break;
	                            }
	                   }	

	               array.recycle();
	             }
	
	     // *** View ***
	     
	     @Override
	     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	               { super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	               }
 	
	     @Override
	     protected void onLayout(boolean changed, int left, int top, int right,int bottom) 
	               { super.onLayout(changed,left,top,right,bottom);

	               	 int   w  = right  - left;
	               	 int   h  = bottom - top;
	               	 float tw = w - (labelMarginLeft + labelMarginRight + labelPaddingLeft + labelPaddingRight);
	               	 float ty = text.getLineBaseline(0) - textMiddle; 
	              
	               	 if (tw < textWidth)
	               	 	{ textWidth = tw;
		    	          text      = new StaticLayout(label,
                                                       0,
                                                       label.length(),
                                                       new TextPaint(paintx),
                                                       10000,
                                                       Layout.Alignment.ALIGN_NORMAL,
                                                       1,
                                                       0,
                                                       true,
                                                       TextUtils.TruncateAt.END,
                                                       (int) textWidth);
	               	 	}
	              
	               	 path.reset ();
	               	 path.moveTo(labelMarginLeft,  ty);
	               	 path.lineTo(strokeWidth/2,    ty);
	               	 path.lineTo(strokeWidth/2,    h-1-strokeWidth/2);
	               	 path.lineTo(w-1-strokeWidth/2,h-1-strokeWidth/2);
	               	 path.lineTo(w-1-strokeWidth/2,ty);
	               	 path.lineTo(labelMarginLeft + labelPaddingLeft + textWidth + labelPaddingRight - cornerRadius,ty);
	               }

	     @Override
	     protected void dispatchDraw(Canvas canvas) 
	               { canvas.save   ();
	                 canvas.translate(labelMarginLeft + labelPaddingLeft,0);
	                 text.draw     (canvas);
	                 canvas.restore();
	                 
	                 canvas.drawPath(path,paint);
	                 
	                 super.dispatchDraw(canvas);
	               }
       }
