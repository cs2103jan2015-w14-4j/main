import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;


public class TaskTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private ArrayList<Task> taskArray;
	protected ArrayList<String> columnNames;
	protected Class[] columnClasses;
	protected Boolean[] isColumnEditable;
	private Class rowClass = Object.class;
	private boolean isModelEditable = true;
	
	
	private static final long serialVersionUID = 1L;
	
	
//	public TaskTableModel(ArrayList<Task> taskArray){
//		this.taskArray = new ArrayList<Task>(taskArray);
//	}
	
	/**
	 *  Full Constructor for creating a <code>RowTableModel</code>.
	 *
	 *  Each item in the <code>modelData</code> List must also be a List Object
	 *  containing items for each column of the row.
	 *
	 *  Each column's name will be taken from the <code>columnNames</code>
	 *  List and the number of columns is determined by the number of items
	 *  in the <code>columnNames</code> List.
	 *
	 *  @param modelData	the data of the table
	 *  @param columnNames	<code>List</code> containing the names
	 *						of the new columns
	 *  @param rowClass     the class of row data to be added to the model
	 */
	
	public TaskTableModel(ArrayList<Task> modelData, ArrayList<String> columnNames, Class rowClass)
	{
		setDataAndColumnNames(modelData, columnNames);
		setRowClass( rowClass );
	}
	
	protected void setDataAndColumnNames(ArrayList<Task> modelData, ArrayList<String> columnNames)
	{
		taskArray = modelData;
		this.columnNames = columnNames;
		columnClasses = new Class[getColumnCount()];
		isColumnEditable = new Boolean[getColumnCount()];
		fireTableStructureChanged();
	}
	
	protected void setRowClass(Class rowClass)
	{
		this.rowClass = rowClass;
	}
	
	public void setColumnClass(int column, Class columnClass)
	{
		columnClasses[column] = columnClass;
		fireTableRowsUpdated(0, getRowCount() - 1);
	}
	
	  @Override
      public int getRowCount() {
          return taskArray.size();
      }

      @Override
  	public int getColumnCount()
  	{
  		return columnNames.size();
  	}
      
  	public Class getColumnClass(int column)
  	{
  		Class columnClass = null;

  		//  Get the class, if set, for the specified column

  		if (column < columnClasses.length)
  			columnClass = columnClasses[column];

  		//  Get the default class

  		if (columnClass == null)
  			columnClass = super.getColumnClass(column);

  		return columnClass;
  	}
/*
      @Override
      public Class<?> getColumnClass(int columnIndex) {
          Class type = String.class;
          switch (columnIndex) {
              case 0://TID
            	  type = Integer.class;
            	  break;
              case 1://TaskName
            	  break;
              case 2://dateFrom
            	  type = Date.class;
            	  break;
              case 3://dateTo
                  type = Date.class;
                  break;
              case 4://deadline
            	  type = Date.class;
                  break;
              case 5://location
            	  break;
              case 6://details
            	  break;
              case 7://priority
            	  type = Integer.class;
            	  break;
              		
          }
          return type;
      }
      */
      
      public String getColumnName(int column)
  	{
  		Object columnName = null;

  		if (column < columnNames.size())
  		{
  			columnName = columnNames.get( column );
  		}

  		return (columnName == null) ? super.getColumnName( column ) : columnName.toString();
  	}
      
  	public boolean isCellEditable(int row, int column)
  	{
  		Boolean isEditable = null;

  		//  Check is column editability has been set

  		if (column < isColumnEditable.length)
  			isEditable = isColumnEditable[column];

  		return (isEditable == null) ? isModelEditable : isEditable.booleanValue();
  	}
      


      @Override
      public Object getValueAt(int rowIndex, int columnIndex) {
         Task task = taskArray.get(rowIndex);
          Object value = null;
          switch (columnIndex) {
	          case 0:
	              value = task.getTID();
	              break;
          	  case 1:
                  value = task.getTaskName();
                  break;
              case 2:
                  value = task.getDateFrom();
                  break;
              case 3:
                  value = task.getDateTo();
                  break;
              case 4:
                  value = task.getDeadline();
                  break;
              case 5:
                  value = task.getLocation();
                  break;
              case 6:
                  value = task.getDetails();
                  break;
              case 7:
                  value = task.getPriority();
                  break;
          }
          return value;
      }  
      
  	/**
  	 *  Adds a row of data to the end of the model.
  	 *  Notification of the row being added will be generated.
  	 *
  	 * @param   rowData		 data of the row being added
  	 */
  	public void addRow(Task rowData)
  	{
  		insertRow(getRowCount(), rowData);
  	}
  	
	public void insertRow(int row, Task rowData)
	{
		taskArray.add(row, rowData);
		fireTableRowsInserted(row, row);
	}
      
	public Task getRow(int row)
	{
		return taskArray.get( row );
	}
	
	/**
	 *  Insert multiple rows of data at the <code>row</code> location in the model.
	 *  Notification of the row being added will be generated.
	 *
	 * @param   row	  row in the model where the data will be inserted
	 * @param   rowList  each item in the list is a separate row of data
	 */
	public void insertRows(int row, ArrayList<Task> rowList)
	{
		taskArray.addAll(row, rowList);
		fireTableRowsInserted(row, row + rowList.size() - 1);
	}
 
      /*
      public void setValueAt(Object aValue, int rowIndex, int columnIndex){
    	  
    	  taskArray.get(rowIndex).get(columnIndex) = aValue;
    	 fireTableCellUpdated(rowIndex, columnIndex); 
      }
      */
      
  }     

