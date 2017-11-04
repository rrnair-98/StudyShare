/*@Author Dhananjay
* this is a custom tree view with our required functionalityr
* 1.returns the selected files
*
* Our format is {absolutepath:{}}*/
package ui.resources;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

import java.util.ArrayList;
import java.util.Stack;

public class CustomDirectory extends TreeView<String> {
    /*Objects for Internal use*/
    private String fileStructure;
    private CheckBoxTreeItem<String> directoryRoot,c1,c2;
    private ArrayList<String> fileList;


   public CustomDirectory(String fileStructure){
        initializeAll(fileStructure);/*Initialize All the objects*/

    }

    private void initializeAll(String fileStructure){
        /*this objects initlization*/
        this.fileStructure=fileStructure;
        this.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
        this.fileList=new ArrayList<>();
        directoryRoot=makeTree(fileStructure,true);
        this.setRoot(directoryRoot);
        this.directoryRoot.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(), new EventHandler<CheckBoxTreeItem.TreeModificationEvent<String>>() {
            @Override
            public void handle(CheckBoxTreeItem.TreeModificationEvent<String> event) {
                CheckBoxTreeItem<String> treeItem=event.getTreeItem();
                String value=getFullPath(treeItem);
                if(event.wasSelectionChanged()){
                    if(treeItem.isLeaf()) {
                        if (treeItem.isSelected()) {
                            {
                                System.out.println(treeItem.getParent().toString());
                                if (!fileList.contains(value))
                                    fileList.add(value);
                            }
                        } else {
                            fileList.remove(value);
                        }
                    }
                }
                System.out.println("FOR ROOT******************");
                for(String files:fileList){
                    System.out.println("Files: "+files);
                }
            }
        });
    }

    private String getFullPath(CheckBoxTreeItem<String> item){
        String fullPath=item.getValue();
        CheckBoxTreeItem<String> current=item;
        while(current.getParent()!=null){
            current=(CheckBoxTreeItem<String>) current.getParent();
            fullPath=current.getValue().concat("/"+fullPath);
        }
        return fullPath;
    }

    private CheckBoxTreeItem<String> makeTree(String s,boolean isRoot){
        CheckBoxTreeItem<String> root;
        int trimIndex,arrayIndex=0;
        Stack<String> stack=new Stack();
        String str=s;
        String strArray[]=new String[50];
        String nodeString;

        if(str.indexOf(':')==-1){
            root=new CheckBoxTreeItem(str);
            root.setGraphic(getIcon(str));
            //if(!isRoot)
                //root.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(),this);
        }
        else {
            nodeString = str.substring(0, str.indexOf(":"));
            root = new CheckBoxTreeItem(nodeString);
            root.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.FOLDER));
            //if(!isRoot)
           // root.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(),this);
            str = str.substring(str.indexOf("{") + 1, str.lastIndexOf("}"));
            trimIndex = 0;
            System.out.println(str);
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '{') {
                    stack.push(new String("{"));
                } else if (str.charAt(i) == '}') {
                    {
                        stack.pop();
                        if (stack.isEmpty()) {
                            strArray[arrayIndex++] = str.substring(trimIndex, i + 1);
                            trimIndex = i + 2;
                            i = i + 2;
                        }
                    }
                } else if (str.charAt(i) == ',') {
                    if (str.substring(trimIndex, i).indexOf(':') == -1) {
                        strArray[arrayIndex++] = str.substring(trimIndex, i);
                        trimIndex = i + 1;
                    }
                } else if (i == str.length() - 1) {
                    strArray[arrayIndex++] = str.substring(trimIndex, i + 1);
                }
            }
            System.out.println("After this" + strArray[arrayIndex-1]);
            for (int i = 0; i < arrayIndex; i++) {
                root.getChildren().add(makeTree(strArray[i],false));
            }
        }

        return root;
    }

    public ArrayList<String> getFileList() {
        return fileList;
    }

    Node getIcon(String fileName){
        try {
                String extension = fileName.substring(fileName.indexOf(".") + 1);
                if (extension.equals("txt"))
                    return new FontAwesomeIconView(FontAwesomeIcon.FILE_TEXT_ALT);
                else if (extension.equals("jpg"))
                    return new FontAwesomeIconView(FontAwesomeIcon.FILE_PHOTO_ALT);
                else if (extension.equals("mp3"))
                    return new FontAwesomeIconView(FontAwesomeIcon.FILE_SOUND_ALT);
                else if (extension.equals("mp4"))
                    return new FontAwesomeIconView(FontAwesomeIcon.FILE_VIDEO_ALT);
                else if (extension.equals("java"))
                    return new FontAwesomeIconView(FontAwesomeIcon.FILE_CODE_ALT);
                else
                    return new FontAwesomeIconView(FontAwesomeIcon.FILE_ALT);
        }
        catch (Exception e){
            return  new FontAwesomeIconView(FontAwesomeIcon.FOLDER);
        }
    }
}
