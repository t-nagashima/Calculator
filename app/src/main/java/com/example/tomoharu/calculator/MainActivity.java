package com.example.tomoharu.calculator;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class MainActivity extends ActionBarActivity {

    String strTemp="";
    String strResult="0";
    String strDisplay = "";
    int operator=0;
    int etSelection=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText et = (EditText)findViewById(R.id.displayPanel);
        et.setFocusable(false);
        et.setHeight(et.getHeight());

        showNumber(strTemp);
        readPreferences();
    }

    @Override
    protected void onStop() {
        super.onStop();
        writePreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void numKeyOnClick(View v){
        String strInKey = (String) ((Button)v).getText();

        if(strInKey.equals(".")){
            if(strTemp.length()==0){
                strTemp="0.";
                etSelection += 2;
            }else{
                if(strTemp.indexOf(".")==-1){
                    strTemp=strTemp+".";
                    strDisplay = strDisplay+".";
                    etSelection += 1;
                }
            }
        }else{
            strTemp=strTemp+strInKey;
            strDisplay = strDisplay + strInKey;
            etSelection += 1;
        }

       // showNumber(strTemp);
        ((EditText)findViewById(R.id.displayPanel)).setText(strDisplay);
        ((EditText)findViewById(R.id.displayPanel)).setSelection(etSelection);
    }

    public void functionKeyOnClick(View v){
        switch(v.getId()){
            case R.id.BtAC:
                strTemp="";
                strDisplay="";
                strResult="0";
                operator = 0;
                etSelection = 0;
                break;
            case R.id.BtC:
                strTemp="";
                break;
        }

        //showNumber(strDisplay);
        ((EditText)findViewById(R.id.displayPanel)).setText(strDisplay);
        ((EditText)findViewById(R.id.displayPanel)).setSelection(etSelection);
    }

    public void operatorKeyOnClick(View v){
        String strInKey = (String) ((Button)v).getText();
        if(operator!=0){
            if(strTemp.length()>0){
                strResult = doCalc();
                //showNumber(strResult);
            }
        }else{
            if(strTemp.length()>0){
                strResult=strTemp;
            }
        }

        strDisplay = strDisplay + strInKey;
        etSelection += 1;
        strTemp = "";
        ((TextView)findViewById(R.id.displayPanel)).setText(strDisplay);

        if(v.getId()==R.id.BtEq){
            operator = 0;
            strDisplay = strDisplay + strResult + "\n";
            etSelection = etSelection + 1 + strResult.length();
            //showNumber(strDisplay);
            ((EditText)findViewById(R.id.displayPanel)).setText(strDisplay);
            ((EditText)findViewById(R.id.displayPanel)).setSelection(etSelection);
        }else{
            operator = v.getId();
        }

    }

    private void showNumber(String strNum){
        DecimalFormat form = new DecimalFormat("#,##0");
        String strDecimal="";
        String strInt="";
        String fText="";

        if(strNum.length() > 0){
            int decimalPoint = strNum.indexOf(".");
            if(decimalPoint>-1){
                strDecimal = strNum.substring(decimalPoint);
                strInt = strNum.substring(0, decimalPoint);
            }else{
                strInt = strNum;
            }
            fText = form.format(Double.parseDouble(strInt))+strDecimal;
        }else fText = "0";

        ((TextView)findViewById(R.id.displayPanel)).setText(fText);

    }

    private String doCalc(){
        BigDecimal bd1 = new BigDecimal(strResult);
        BigDecimal bd2 = new BigDecimal(strTemp);
        BigDecimal result = BigDecimal.ZERO;

        switch(operator){
            case R.id.BtPlu:
                result = bd1.add(bd2);
                break;
            case R.id.BtMin:
                result = bd1.subtract(bd2);
                break;
            case R.id.BtMul:
                result = bd1.multiply(bd2);
                break;
            case R.id.BtDiv:
                if(!bd2.equals(BigDecimal.ZERO)) {
                    result = bd1.divide(bd2, 12, 3);
                }else{
                    Toast toast = Toast.makeText(this, "Don't divide by zero!", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
        }

        if(result.toString().indexOf(".")>=0){
            return result.toString().replaceAll("\\.0+$|0+$", "");
        }else{
            return result.toString();
        }
    }

    private void writePreferences(){
        SharedPreferences prefs = getSharedPreferences("CalcPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("strTemp", strTemp);
        editor.putString("strResult", strResult);
        editor.putInt("operator", operator);
        editor.putString("strDisplay", ((TextView)findViewById(R.id.displayPanel)).getText().toString());
    }

    private void readPreferences(){
        SharedPreferences prefs = getSharedPreferences("CalcPrefs", MODE_PRIVATE);
        strTemp = prefs.getString("strTemp", "");
        strResult = prefs.getString("strResult", "0");
        operator = prefs.getInt("operator", 0);
        ((TextView)findViewById(R.id.displayPanel)).setText(prefs.getString("strDisplay", "0"));
    }
}
