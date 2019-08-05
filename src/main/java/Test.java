import java.util.ArrayList;
import java.util.List;

/**
 * @author Sui
 * @date 2019.07.02 16:22
 */
public class Test {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("local130");
        list.add("#{local131}");
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).startsWith("#")){
                list.set(i, list.get(i).replace("#{", "").replace("}",""));
            }
        }
        System.out.println(list);

    }


}
