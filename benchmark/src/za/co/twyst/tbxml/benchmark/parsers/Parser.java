package za.co.twyst.tbxml.benchmark.parsers;

public abstract class Parser 
       { // CONSTANTS
	
	     protected static final String DOCUMENT    = "document";
	     protected static final String SECTION     = "section";
	     protected static final String ITEM        = "item";
	     protected static final String ID          = "id";
	     protected static final String NAME        = "name";
	     protected static final String RATED       = "rated";
	     protected static final String GRADE       = "grade";
	     protected static final String STARS       = "stars";
	     protected static final String ORDER       = "order";
	     protected static final String DESCRIPTION = "description";
	     protected static final String ORIGINATED  = "originated";
	     protected static final String CHECKED     = "checked";
	     protected static final String BY          = "by";
	     protected static final String DATE        = "date";

	     protected static final String ATTR_ID     = "@id";
	     protected static final String ATTR_NAME   = "@name";
	     protected static final String ATTR_RATED  = "@rated";
	     protected static final String ATTR_GRADE  = "@grade";
	     protected static final String ATTR_STARS  = "@stars";
	     protected static final String ATTR_ORDER  = "@order";
	     protected static final String ATTR_BY     = "@by";
	     protected static final String ATTR_DATE   = "@date";

	     protected static final String TEXT_DESCRIPTION = "description/text()";

	     public abstract long parse(int iterations) throws Exception;
       }
