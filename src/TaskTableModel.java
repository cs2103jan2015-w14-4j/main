import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;


public class TaskTableModel extends AbstractTableModel {
	
	private ArrayList<Task> taskArray;
	
	public TaskTableModel(ArrayList<Task> taskArray){
		this.taskArray = new ArrayList<Task>(taskArray);
	}
	
	  @Override
      public int getRowCount() {
          return taskArray.size();
      }

      @Override
      public int getColumnCount() {
          return 7;
      }

      @Override
      public String getColumnName(int column) {
          String name = "??";
          switch (column) {
              case 0:
                  name = "Task Name";
                  break;
              case 1:
                  name = "Date From";
                  break;
              case 2:
                  name = "Date To";
                  break;
              case 3:
                  name = "Deadline";
                  break;
              case 4:
                  name = "Location";
                  break;
              case 5:
                  name = "Details";
                  break;
              case 6:
                  name = "Priority";
                  break;
          }
          return name;
      }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
          Class type = String.class;
          switch (columnIndex) {
              case 0://TID
            	  type = Integer.class;
              case 1://TaskName
              case 2://dateFrom
            	  type = Date.class;
              case 3://dateTo
                  type = Date.class;
                  break;
              case 4://deadline
            	  type = Date.class;
                  break;
              case 5://location
              case 6://details
              case 7://priority
            	  type = Integer.class;
            	  break;
              		
          }
          return type;
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
  }     

