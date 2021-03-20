import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class solution {

    class imu {
        List<String> imuBuf;
        double ACC_X = 0.0, ACC_Y = 0.0, ACC_Z = 0.0, GYR_X = 0.0, GYR_Y = 0.0, GYR_Z = 0.0;
        double MAG_X = 0.0, MAG_Y = 0.0, MAG_Z = 0.0, PITCHX = 0.0, ROLLY = 0.0, YAWZ = 0.0, PRES = 0.0;
        int ACCE_NUM, GYRO_NUM, MAGN_NUM, PRES_NUM, AHRS_NUM;
        int dataTypeNum;
        double SensorTimeStamp;

        imu() {
            imuBuf = new ArrayList<>();
            clean();
        }

        public void process(String strs[]) {
            switch (strs[0]) {
            case "ACCE":
                ACC_X += Double.parseDouble(strs[3]);
                ACC_Y += Double.parseDouble(strs[4]);
                ACC_Z += Double.parseDouble(strs[5]);
                if (ACCE_NUM == 0) {
                    dataTypeNum++;
                }
                ACCE_NUM++;
                break;
            case "GYRO":
                GYR_X += Double.parseDouble(strs[3]);
                GYR_Y += Double.parseDouble(strs[4]);
                GYR_Z += Double.parseDouble(strs[5]);
                if (GYRO_NUM == 0) {
                    dataTypeNum++;
                }
                GYRO_NUM++;
                break;
            case "PRES":
                PRES += Double.parseDouble(strs[3]);
                if (PRES_NUM == 0) {
                    dataTypeNum++;
                }
                PRES_NUM++;
                break;
            case "AHRS":
                GYR_X += Double.parseDouble(strs[3]);
                GYR_Y += Double.parseDouble(strs[4]);
                GYR_Z += Double.parseDouble(strs[5]);
                if (GYRO_NUM == 0) {
                    dataTypeNum++;
                }
                GYRO_NUM++;
                break;
            case "MAGN":
                MAG_X += Double.parseDouble(strs[3]);
                MAG_Y += Double.parseDouble(strs[4]);
                MAG_Z += Double.parseDouble(strs[5]);
                if (MAGN_NUM == 0) {
                    dataTypeNum++;
                }
                MAGN_NUM++;
                break;
            }
            SensorTimeStamp = Math.min(SensorTimeStamp, Double.parseDouble(strs[2]));
            //如果五种数据都有了进行聚合，对重复的字段进行求和
            if(dataTypeNum == 5){
                
            }
        }

        public void clean() {
            ACC_X = 0.0;
            ACC_Y = 0.0;
            ACC_Z = 0.0;
            GYR_X = 0.0;
            GYR_Y = 0.0;
            GYR_Z = 0.0;
            MAG_X = 0.0;
            MAG_Y = 0.0;
            MAG_Z = 0.0;
            PITCHX = 0.0;
            ROLLY = 0.0;
            YAWZ = 0.0;
            PRES = 0.0;
            ACCE_NUM = GYRO_NUM = MAGN_NUM = AHRS_NUM = PRES_NUM = 0;
            dataTypeNum = 0;
            SensorTimeStamp = Double.MAX_VALUE;
        }
    }

    List<String> wifiBuf, gnssBuf;// 进行缓存

    public static void main(String[] args) {
        solution s = new solution();
        s.imuBuf = new ArrayList<>();
        s.wifiBuf = new ArrayList<>();
        s.gnssBuf = new ArrayList<>();
        String path = new String("C:\\Users\\xxy\\Desktop\\logfile_2020_03_19_09_31_56.txt");
        // 1.读取处理文件
        File file = new File(path);
        s.ReadAndProcess(file);
    }

    // 文件读取程序
    public void ReadAndProcess(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));// 读取文件
            String s = null;
            while ((s = br.readLine()) != null) {// 按行读取
                // 1.跳过注释
                if (s.charAt(0) == '%') {
                    continue;
                } // 进行处理
                else {
                    String strs[] = s.split(";");// 将有效数据按;分割
                    process(strs, s);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 处理程序
    public void process(String[] strs, String s) {
        for (int i = 0; i < strs.length; i++) {
            switch (strs[0]) {
            case "ACCE":
            case "GYRO":
            case "MAGN":
            case "PRES":
            case "AHRS":
                imu(s);
                break;
            case "WIFI":
                wifi(s);
                break;
            case "GNSS":
                gnss(s);
                break;
            }
        }
    }

}