package Carculator.Class;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Control {
    /***文本框***/
    private StringBuffer sb = new StringBuffer();
    private String Output;
    private Map<String,String> map = new HashMap<String,String>();
    /***计算***/
    private String FirstInput = null;
    private String operator = null;
    private Double Result = null;
    private boolean Cac_flag = false;//完成一次计算后标志位
    /***历史记录--顺序表***/
    private ObservableList <String> list = FXCollections.observableArrayList();
    private String Memory[] = new String[Const_.Memory_capacity];
    private int index = 0;

    @FXML
    private ListView List;//记忆功能链表（FX
    @FXML
    private VBox box;//记忆功能的背景
    @FXML
    private Label result;//计算器结果标签
    @FXML
    private Label expression;
    @FXML
    public void onClicked(Event e){
        //第一次输入的结果转数字
        Double a;
        //第二次输入的结果转数字
        Double b;
        //获取监控目标
        javafx.scene.control.Button button = (javafx.scene.control.Button) e.getTarget();
        String ClickString = button.getText();//Save button`s text

        if("0123456789".contains(ClickString)){
            if(Cac_flag){
                sb.delete(0,sb.length());
                Cac_flag = false;
            }
            sb.append(ClickString);
        }else if(ClickString.equals(".")&&!sb.isEmpty()){
            if(sb.indexOf(".") == -1){
                sb.append(ClickString);
            }
        }else if(ClickString.matches("[\\+\\-*/%^]{1}")){
            if(sb.isEmpty()){
                operator = ClickString;
            }else{
                operator = ClickString;
                FirstInput = sb.toString();
                sb.delete(0, sb.length());
            }
        }else if(ClickString.matches("[//]{2}")){
            operator = ClickString;
            FirstInput = sb.toString();
            sb.delete(0, sb.length());
        }else if(ClickString.matches("√{1}")&&!sb.isEmpty()){
            operator = ClickString;
            FirstInput = sb.toString();
            a = Double.parseDouble(sb.toString());
            sb.delete(0, sb.length());
            if(a>0)
                Result = Math.sqrt(a);
            else Result = 0.0;
            sb.append(Result);
            if(operator.equals("√")){
                Memory[index] = operator+a+"="+Result;
                System.out.println(Memory[index]);
                list.add((index+1)+" | "+Memory[index]);
            }
           index = (index+1)%16;
        }else if(ClickString.matches("[COSSINTAN]{3}")&&!sb.isEmpty()){
            operator = ClickString;
            FirstInput = sb.toString();
            a = Double.parseDouble(sb.toString());
            sb.delete(0, sb.length());
            double radians = Math.toRadians(a);//转角度
            if(operator.equals("COS")){
                Result = Math.cos(radians);
            }else if(operator.equals("SIN")){
                Result = Math.sin(radians);
            }else if(operator.equals("TAN")){
                Result = Math.tan(radians);
            }
            sb.append(Result.toString());
            if(operator.matches("[COSSINTAN]{3}")){
                Memory[index] = operator+FirstInput+"°"+"="+Result;
                System.out.println(Memory[index]);
                list.add((index+1)+" | "+Memory[index]);
            }
            index = (index+1)%16;
        }else if(ClickString.equals("+/-")&&!sb.isEmpty()){
            FirstInput = sb.toString();
            a = Double.parseDouble(sb.toString());
            sb.delete(0, sb.length());
            a =-a;
            FirstInput = a.toString();
            sb.append(FirstInput);
        }else if(ClickString.equals("1/X")&&!sb.isEmpty()){
            FirstInput = sb.toString();
            a = Double.parseDouble(sb.toString());
            sb.delete(0, sb.length());
            if(a == 0){
                FirstInput = "0";
            }else{
                a = 1 / a;
                FirstInput = a.toString();
                sb.append(FirstInput);
            }
        }else if(ClickString.equals("|X|")&&!sb.isEmpty()){
            FirstInput = sb.toString();
            a = Double.parseDouble(sb.toString());
            sb.delete(0, sb.length());
            if(a>0){
                a = a;
            }else{
                a = -a;
            }
            FirstInput = a.toString();
            sb.append(FirstInput);
        }else if(ClickString.equals("=")&&FirstInput!=null){
            a = Double.parseDouble(FirstInput);
            if(!result.getText().isEmpty()){
                b = Double.parseDouble(result.getText());
            }else{
                b = a;
            }
            switch (operator){
                case "+":
                    Result = a+b;
                    break;
                case "-":
                    Result = a-b;
                    break;
                case "*":
                    Result = a*b;
                    break;
                case "/":
                    if(b!=0)Result = a/b;
                    else Result = 0.0;
                    break;
                case "%":
                    Result = a%b;
                    break;
                case "//":
                    if(b!=0){
                        int result1 = (int)(a/b);
                        Result = (double) result1;
                    }
                    break;
                case "^":
                    Result = Math.pow(a,b);
                    break;
            }
            sb.delete(0, sb.length());
            sb.append(Result.toString());
            if(operator.matches("[\\+\\-*/%^]{1}")||operator.equals("//")){
                Memory[index] = a+operator+b+"="+Result;
                System.out.println(Memory[index]);
                list.add((index+1)+" | "+Memory[index]);
            }
            index = (index+1)%16;
            Cac_flag = true;
        }else if(ClickString.equals("Clear")){
            FirstInput = null;
            operator = null;
            sb.delete(0, sb.length());
        }else if(ClickString.equals("Delete")){
            if(!sb.isEmpty()){
                sb.deleteCharAt(sb.length()-1);
            }
        }else if(ClickString.equals("清空历史")){
            index = 0;
            list.clear();
        }
        setOutput();
        if(index==0){
            list.clear();
        }
    }

    /***监控初始化***/
    public void initialize(){
        StringBind();//将指定的计算器按钮中的text写进哈希表中
        List.setItems(list);//将
        box.setPadding(new Insets(11));
    }
    /***按键文本录入哈希表***/
    public void StringBind(){
        for(int i = 0;i<Const_.names.length;i++){
            for(int j = 0;j<Const_.names[i].length;j++){
                map.put(Const_.target[i][j],Const_.names[i][j]);
            }
        }
    }
    /***结果写进文本显示框***/
    public void setOutput(){
        Output = "";
        for(int i = 0; i < sb.length(); i++){
            Output = Output + map.get(String.valueOf(sb.charAt(i)));//因为char不能转化成string
        }
        result.setText(Output);
    }
}
