import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class solution{
    List<String> imuBuf,wifiBuf,gnssBuf;//进行缓存
    double SensorTimeStamp = Double.MAX_VALUE;//记录最小时间
    public static void main(String[] args) {
        solution s = new solution();
        s.imuBuf = new ArrayList<>();
        s.wifiBuf = new ArrayList<>();
        s.gnssBuf = new ArrayList<>();
        String path = new String("C:\\Users\\xxy\\Desktop\\logfile_2020_03_19_09_31_56.txt");
        //1.读取处理文件
        File file = new File(path);
        s.ReadAndProcess(file);
    }

    //文件读取程序
    public void ReadAndProcess(File file){
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//读取文件
            String s = null;
            while((s = br.readLine())!=null){//按行读取
                //1.跳过注释
                if(s.charAt(0)=='%'){
                    continue;
                }//进行处理
                else{
                    String strs[] = s.split(";");//将有效数据按;分割
                    process(strs);
                }
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //处理程序
    public void process(String []strs){
        for (int i = 0; i < strs.length; i++) {
            switch(strs[0]){
                case "ACCE":
                case "GYRO":
                case "MAGN":
                case "PRES":
                case "AHRS":
                    imu(strs)
            }
        }
    }

    //imu处理程序
    public void imu(String s){

    }

    //wifi处理程序
    public void wifi(String s){

    }

    //gnss处理程序
    public void gnss(String s){

    }


}