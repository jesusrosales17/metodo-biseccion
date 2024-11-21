package metodobiseccion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.table.DefaultTableModel;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Biseccion {

    private float xa;
    private float xb;
    private String function;
    private float rows[][] = new float[0][7];

    public void setXa(float value) {
        xa = value;
    }

    public void setXb(float value) {
        xb = value;
    }

    public void setFunction(String fn) {
        function = fn;
    }

    public float getXa() {
        return xa;
    }

    public float getXb() {
        return xb;
    }

    public String getFunction() {
        return function;
    }

    public DefaultTableModel StartMethod(DefaultTableModel modelTable) {

        // Cuando se detendra
        boolean stop = false;

        do {
            Expression xaEvaluation = new ExpressionBuilder(function)
                    .variables("x")
                    .build()
                    .setVariable("x", xa);
            Expression xbEvaluation = new ExpressionBuilder(function)
                    .variables("x")
                    .build()
                    .setVariable("x", xb);

            float fxa = new BigDecimal(xaEvaluation.evaluate()).setScale(3, RoundingMode.HALF_UP).floatValue();
            float fxb = new BigDecimal(xbEvaluation.evaluate()).setScale(3, RoundingMode.HALF_UP).floatValue();

            float xr = new BigDecimal((xa + xb) / 2).setScale(3, RoundingMode.HALF_UP).floatValue();
            System.out.println("xr: " + xr);
            Expression xrEvaluation = new ExpressionBuilder(function)
                    .variables("x")
                    .build()
                    .setVariable("x", xr);

            float fxr = new BigDecimal(xrEvaluation.evaluate()).setScale(3, RoundingMode.HALF_UP).floatValue();

            float ep = 0;
            System.out.println("rows: " + rows.length);
            if (rows.length > 0) {
                ep = new BigDecimal( ((xr - rows[rows.length - 1][4]) / xr) * 100).setScale(3, RoundingMode.HALF_UP).floatValue();
                System.out.println("Error de Porcentaje: " + ep);
            }
          

            // Respaldo de la matriz
            float backup[][] = new float[rows.length][7];
            for (int i = 0; i < rows.length; i++) {
                for (int j = 0; j < rows[i].length; j++) {             
                        backup[i][j] = rows[i][j];
                }
            }

            // redimensionar
            rows = new float[backup.length + 1][7];

            //regresar los valores
            for (int i = 0; i < backup.length; i++) {
                for (int j = 0; j < backup[i].length; j++) {
                    rows[i][j] = backup[i][j];
                }
            }

            // Agregar el nuevo elemento
          
            rows[rows.length - 1][0] = xa;
            rows[rows.length - 1][1] = xb;
            rows[rows.length - 1][2] = fxa;
            rows[rows.length - 1][3] = fxb;
            rows[rows.length - 1][4] = xr;
            rows[rows.length - 1][5] = fxr;
            rows[rows.length - 1][6] = ep;

            xa = xr;

            // detener si el ep = 0
            if (rows.length > 1) {
                stop = (ep == 0);
        
            }

            int nrepeat = 0;
            // detener si el xr se repite
            for (int i = 0; i < rows.length; i++) {
                if (rows[i][4] == xr) {
                    nrepeat++;
                    if (nrepeat == 2) {
                        stop = true;
                       
                    }
                }
            }

        } while (!stop);

//        for (int i = 0; i < rows.length; i++) {
//            for (int j = 0; j < rows[i].length; j++) {
//                System.out.println(rows[i][j]);
//            }
//        }
        modelTable = addRowsInModel(modelTable);
//        System.out.println(modelTable.getRowCount());
        return modelTable;
    }

    private DefaultTableModel addRowsInModel(DefaultTableModel model) {
        System.out.println(rows.length);
        for (int i = 0; i < rows.length; i++) {
            Object[] row = new Object[8];
            row[0] = i;
            row[1] = rows[i][0];
            row[2] = rows[i][1];
            row[3] = rows[i][2];
            row[4] = rows[i][3];
            row[5] = rows[i][4];
            row[6] = rows[i][5];
            row[7] = (i == 0) ? "-----" : rows[i][6];

            model.addRow(row);
        }
        return model;
    }
    
    public void clear(DefaultTableModel model) {
        model.setNumRows(0);
        rows = new float[0][7];
    }
}
