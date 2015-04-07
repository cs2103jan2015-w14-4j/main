import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;


public class TaskTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static ArrayList<String[]>taskArrayString;
	protected ArrayList<String> columnNames;
	protected Class[] columnClasses;
	protected Boolean[] isColumnEditable;
	private Class rowClass = Object.class;
	private boolean isModelEditable = true;

	private List<Color> rowColours = Arrays.asList(
			Color.GREEN,
			Color.CYAN
			);


	private static final long serialVersionUID = 1L;

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

	public TaskTableModel(ArrayList<String[]> modelData, ArrayList<String> columnNames, Class rowClass)
	{
		setDataAndColumnNames(modelData, columnNames);
		setRowClass( rowClass );
	}

	protected void setDataAndColumnNames(ArrayList<String[]> modelData, ArrayList<String> columnNames)
	{
		taskArrayString = modelData;
		this.columnNames = columnNames;
		columnClasses = new Class[getColumnCount()];
		isColumnEditable = new Boolean[getColumnCount()];
		fireTableStructureChanged();
	}

	protected void setRowClass(Class rowClass)
	{
		this.rowClass = rowClass;
	}

	/*
	public void setColumnClass(int column, Class columnClass)
	{
		columnClasses[column] = columnClass;
		fireTableRowsUpdated(0, getRowCount() - 1);
	}
	*/

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


	public void setRowColour(int row, Color c) {
		System.out.println("rowCOlours?" + rowColours);
		rowColours.set(row, c);
		fireTableRowsUpdated(row, row);
	}
	public Color getRowColour(int row) {
		return rowColours.get(row);
	}

	@Override
	public int getRowCount() {
		return taskArrayString.size();
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.size();
	}

	/**
	 * Returns the column name.
	 *
	 * @return a name for this column using the string value of the
	 * appropriate member in <code>columnNames</code>. If
	 * <code>columnNames</code> does not have an entry for this index
	 * then the default name provided by the superclass is returned
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
	

	/**
	 * Returns true regardless of parameter values.
	 *
	 * @param   row		   the row whose value is to be queried
	 * @param   column		the column whose value is to be queried
	 * @return				  true
	 */
	public boolean isCellEditable(int row, int column)
	{
		Boolean isEditable = null;

		if (column < isColumnEditable.length)
			isEditable = isColumnEditable[column];

		return (isEditable == null) ? isModelEditable : isEditable.booleanValue();
	}



	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String[] task = taskArrayString.get(rowIndex);
		String value = null;
		value = task[columnIndex];
		return value;
	}  

	/**
	 *  Adds a row of data to the end of the model.
	 *  Notification of the row being added will be generated.
	 *
	 * @param   rowData		 data of the row being added
	 */
	public void addRow(String[] rowData)
	{
		insertRow(getRowCount(), rowData);
	}

	public void insertRow(int row, String[] rowData)
	{
		taskArrayString.add(row, rowData);
		fireTableRowsInserted(row, row);
	}

	public String[] getRow(int row)
	{
		return taskArrayString.get( row );
	}

	/**
	 *  Insert multiple rows of data at the <code>row</code> location in the model.
	 *  Notification of the row being added will be generated.
	 *
	 * @param   row	  row in the model where the data will be inserted
	 * @param   rowList  each item in the list is a separate row of data
	 */
	public void insertRows(int row, ArrayList<String[]> rowList)
	{	
		taskArrayString.addAll(row, rowList);
		fireTableRowsInserted(row, row + rowList.size() - 1);
	}

	public void removeRowRange(int start, int end)
	{
		taskArrayString.subList(start, end ).clear();
		fireTableDataChanged();
	}

	public void removeRows(int... rows)
	{

		for (int i = rows.length - 1; i >= 0; i--)
		{
			int row = rows[i];
			taskArrayString.remove(row);
			fireTableRowsDeleted(row, row);
		}
	}

	public void clearTable()
	{
		removeRowRange(0 ,getRowCount());
	}



	public void refreshTable(ArrayList<String[]> tableData){

		ArrayList<String[]> newData = new ArrayList<String[]>(tableData);
		clearTable();
		insertRows(0, newData);


	}


	/*
      public void setValueAt(Object aValue, int rowIndex, int columnIndex){

    	  taskArray.get(rowIndex).get(columnIndex) = aValue;
    	 fireTableCellUpdated(rowIndex, columnIndex); 
      }
	 */

}     

