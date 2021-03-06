import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class solution {

    public static class imu {
        List<String> imuBuf;
        // 记录下面各类变量的和
        double ACC_X = 0.0, ACC_Y = 0.0, ACC_Z = 0.0, GYR_X = 0.0, GYR_Y = 0.0, GYR_Z = 0.0;
        double MAG_X = 0.0, MAG_Y = 0.0, MAG_Z = 0.0, PITCHX = 0.0, ROLLY = 0.0, YAWZ = 0.0, PRES = 0.0;
        // 记录每种数据的个数
        int ACCE_NUM, GYRO_NUM, MAGN_NUM, PRES_NUM, AHRS_NUM;
        // 记录从上一次输出后收到的数据的类型个数
        int dataTypeNum;
        // 记录最小时间
        double SensorTimeStamp;
        String path;

        imu(String path) {
            imuBuf = new ArrayList<>();
            this.path = path;
            imuBuf = new ArrayList<>();
            clean();// 数据初始化
        }

        // 输出结果
        public void writeFile() {
            File file = new File(path);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
                for (int i = 0; i < imuBuf.size(); i++) {
                    bw.write(imuBuf.get(i));
                    // System.out.println("imuBuf["+i+"]:\t"+imuBuf.get(i));
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            imuBuf.clear();
        }

        // 处理数据
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
                PITCHX += Double.parseDouble(strs[3]);
                ROLLY += Double.parseDouble(strs[4]);
                YAWZ += Double.parseDouble(strs[5]);
                if (AHRS_NUM == 0) {
                    dataTypeNum++;
                }
                AHRS_NUM++;
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
            // 如果五种数据都有了进行聚合，对重复的字段进行求和平均
            if (dataTypeNum == 5) {
                StringBuilder sb = new StringBuilder();
                sb.append(Double.toString((Double.parseDouble(strs[1]) - SensorTimeStamp) * 1000) + ",");
                sb.append(Double.toString(ACC_X / ACCE_NUM) + "," + Double.toString(ACC_Y / ACCE_NUM) + ","
                        + Double.toString(ACC_Z / ACCE_NUM) + ",");
                sb.append(Double.toString(GYR_X / GYRO_NUM) + "," + Double.toString(GYR_Y / GYRO_NUM) + ","
                        + Double.toString(GYR_Z / GYRO_NUM) + ",");
                sb.append(Double.toString(MAG_X / MAGN_NUM) + "," + Double.toString(MAG_Y / MAGN_NUM) + ","
                        + Double.toString(MAG_Z / MAGN_NUM) + ",");
                sb.append(Double.toString(PITCHX / AHRS_NUM) + "," + Double.toString(ROLLY / AHRS_NUM) + ","
                        + Double.toString(YAWZ / AHRS_NUM) + ",");
                sb.append(Double.toString(PRES));
                imuBuf.add(sb.toString());
                clean();
                if (imuBuf.size() == 100)
                    writeFile();
            }
        }

        // 初始化
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

    public static class wifi {
        List<String> wifiBuf;// 暂存结果
        double SensorTimeStamp;// 记录最小时间
        String path;
        int count = 1;

        wifi(String path) {
            wifiBuf = new ArrayList<>();
            this.path = path;
        }

        // 输出结果
        public void writeFile() {
            File file = new File(path);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
                for (int i = 0; i < wifiBuf.size(); i++) {
                    bw.write(wifiBuf.get(i));
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            wifiBuf.clear();
        }

        // 将16进制转换称10进制
        public String SISTEENTO10(String strs[]) {
            long ans = 0;
            for (int i = 0; i < strs.length; i++) {
                for (int j = 0; j < strs[i].length(); j++) {
                    if (strs[i].charAt(j) >= 'a' && strs[i].charAt(j) <= 'f') {
                        ans = ans * 16 + 10 + (strs[i].charAt(j) - 'a');
                    } else {
                        ans = ans * 16 + (strs[i].charAt(j) - '0');
                    }
                }
            }
            return Long.toString(ans);
        }

        public void process(String strs[]) {
            StringBuilder sb = new StringBuilder();
            SensorTimeStamp = Math.min(SensorTimeStamp, Double.parseDouble(strs[2]));// 记录最小值
            double time = Double.parseDouble(strs[2]) - SensorTimeStamp;// 求TIME
            /*
            System.out.println(count + ":\t" + "本身时间:" + Double.parseDouble(strs[2]) + "\tMinSst:" + SensorTimeStamp
                    + "\tTIME:" + time);*/
            sb.append(Double.toString(time) + "," + SISTEENTO10(strs[4].split(":")) + "," + strs[strs.length - 1]);// 拼接字符串
            wifiBuf.add(sb.toString());// 添加结果进缓存
            if (wifiBuf.size() == 100)// 输出
                writeFile();
            count++;
        }
    }

    public static class gnss {
        List<String> gnssBuf;
        double firsTime;
        String path;
        HashMap<Double, List<String>> map;// 用来去重

        gnss(String path, double time) {
            gnssBuf = new ArrayList<>();
            this.path = path;
            firsTime = time;
            map = new HashMap<>();
        }

        // 输出结果
        public void writeFile() {
            File file = new File(path);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
                for (int i = 0; i < gnssBuf.size(); i++) {
                    // System.out.println("gnss["+i+"]:\t"+gnssBuf.get(i));
                    bw.write(gnssBuf.get(i));
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            gnssBuf.clear();
        }

        public void process(String strs[]) {
            StringBuilder sb = new StringBuilder();
            StringBuilder mapStr = new StringBuilder();
            double Ctime = Double.parseDouble(strs[1]) - firsTime;// 计算CTIME
            double sts = Double.parseDouble(strs[2]);
            // 拼接数据
            sb.append(Double.toString(Ctime) + "," + strs[3] + "," + strs[4] + "," + strs[5] + "," + strs[6] + ","
                    + strs[7] + "," + strs[8] + "," + strs[9] + "," + strs[10]);
            // 拼接非AppTimeStamp部分，用于哈希表查询去重
            for (int i = 3; i < strs.length; i++) {
                mapStr.append(strs[i]);
            }
            // 去重
            List<String> list = map.getOrDefault(sts, new ArrayList<>());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(mapStr.toString()))
                    return;
            }
            gnssBuf.add(sb.toString());// 加入缓存
            list.add(mapStr.toString());
            map.put(sts, list);
            // 输出
            if (gnssBuf.size() == 100)
                writeFile();
        }
    }

    public static void main(String[] args) {
        solution s = new solution();
        /*
        String path = new String("C:\\Users\\xxy\\Desktop\\logfile_2020_03_19_09_31_56.txt");// 输入文件路径
        imu IMU = new imu("D:\\ATR实验室研一软件培训结业测试题\\ATR-xxy\\ATR-\\imu1.txt");// 创建IMU对象，处理数据并保存
        wifi WIFI = new wifi("D:\\ATR实验室研一软件培训结业测试题\\ATR-xxy\\ATR-\\wifi1.txt");// 创建WIFI对象，处理数据并保存
        gnss GNSS = new gnss("D:\\ATR实验室研一软件培训结业测试题\\ATR-xxy\\ATR-\\gnss1.txt", -1);// 创建GNSS对象，处理数据并保存
        */
        
        String path = new String("D:\\ATR实验室研一软件培训结业测试题\\ATR-xxy\\ATR-\\EVALUATION(1).txt");// 输入文件路径
        imu IMU = new imu("D:\\ATR实验室研一软件培训结业测试题\\ATR-xxy\\ATR-\\imu2.txt");// 创建IMU对象，处理数据并保存
        wifi WIFI = new wifi("D:\\ATR实验室研一软件培训结业测试题\\ATR-xxy\\ATR-\\wifi2.txt");// 创建WIFI对象，处理数据并保存
        gnss GNSS = new gnss("D:\\ATR实验室研一软件培训结业测试题\\ATR-xxy\\ATR-\\gnss2.txt", -1);// 创建GNSS对象，处理数据并保存
        
        // 读取并处理数据
        File file = new File(path);
        s.getMinSTS(file, WIFI, IMU, GNSS);
        s.ReadAndProcess(file, WIFI, IMU, GNSS);
    }

    // 读取处理数据
    public void ReadAndProcess(File file, wifi WIFI, imu IMU, gnss GNSS) {
        boolean flag = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));// 读取文件
            String s = null;
            while ((s = br.readLine()) != null) {// 按行读取
                // 1.跳过注释
                // System.out.println(s);
                if (s.length() == 0 || s.charAt(0) == '%') {
                    continue;
                } // 进行处理
                else {
                    String strs[] = s.split(";");// 将有效数据按;分割
                    if (strs[0].equals("PRES"))
                        flag = true;// 第一次遇到PRES前的数据都不处理
                    if (GNSS.firsTime == -1)
                        GNSS.firsTime = Double.parseDouble(strs[1]);
                    if (flag)
                        process(strs, s, WIFI, IMU, GNSS);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        IMU.writeFile();
        WIFI.writeFile();
        GNSS.writeFile();
    }

    // 处理程序
    public void process(String[] strs, String s, wifi WIFI, imu IMU, gnss GNSS) {
        switch (strs[0]) {
        case "ACCE":
        case "GYRO":
        case "MAGN":
        case "PRES":
        case "AHRS":
            IMU.process(strs);
            break;
        case "WIFI":
            WIFI.process(strs);
            break;
        case "GNSS":
            GNSS.process(strs);
            break;
        }
    }

    // 获得所有数据中最小SensorTimeStamp
    public void getMinSTS(File file, wifi WIFI, imu IMU, gnss GNSS) {
        double minSts = Double.MAX_VALUE;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));// 读取文件
            String s = null;
            while ((s = br.readLine()) != null) {// 按行读取
                // 1.跳过注释
                if (s.length() == 0 || s.charAt(0) == '%') {
                    continue;
                } // 进行处理
                else {
                    String strs[] = s.split(";");// 将有效数据按;分割
                    if (strs[0].equals("WIFI") || strs[0].equals("MAGN") || strs[0].equals("ACCE")
                            || strs[0].equals("GYRO") || strs[0].equals("PRES") || strs[0].equals("GNSS")
                            || strs[0].equals("AHRS")) {
                        minSts = Math.min(minSts, Double.parseDouble(strs[2]));
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WIFI.SensorTimeStamp = IMU.SensorTimeStamp = minSts;
    }

}