import javax.jws.Oneway;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private Account LoginAccount = new Account();

    public void start() {
        while (true) {
            System.out.println("-------欢迎来到ATM系统------");
            System.out.println("请选择要办理的业务");
            System.out.println("1.登录");
            System.out.println("2.开户");
            int commend = sc.nextInt();
            switch (commend) {
                case 1:
                    login();
                    break;
                case 2:
                    creatAccount();
                    break;
                default:
                    System.out.println("您输入的数字有误，请重新输入");
            }
        }
    }

    //登录
    private void login() {
        if(accounts.size()==0){
            System.out.println("系统中暂无账户");
            return;
        }
        System.out.println("----登录界面-----");
        while (true) {
            System.out.println("请输入您的卡号：");
            String cardid = sc.next();
            Account acc = getAccountByCardId(cardid);
            if (acc == null){
                System.out.println("您输入的账户不存在，请重新输入");
            }else{
                while (true) {
                    System.out.println("请输入您的密码");
                    String password = sc.next();
                    if(password.equals(acc.getPassword())){
                        System.out.println("恭喜您，登录成功");
                        LoginAccount = acc;
                        showUserCommend();
                        return;
                    }else{
                        System.out.println("密码输入错误，请重新输入");
                    }
                }
            }
        }
    }

    // 展示登录页面信息
    private void showUserCommend() {
        while (true) {
            System.out.println(LoginAccount.getUsername()+"欢迎进入用户界面--");
            System.out.println("1、查询账户");
            System.out.println("2、存款");
            System.out.println("3、取款");
            System.out.println("4、转账");
            System.out.println("5、密码修改");
            System.out.println("6、退出");
            System.out.println("7、注销当前账户");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    showLoginAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    drawMoney();
                    break;
                case 4:
                    transferMoney();
                    break;
                case 5:
                    updatePassword();
                    return;// 跳出并结束当前方法
                case 6:
                    System.out.println(LoginAccount.getUsername() + "您退出系统成功！");
                    return; // 跳出并结束当前方法
                case 7:
                    if(removeAccount()){
                        return;
                    }
                    break;
                default:
                    System.out.println("您当前选择的操作是不存在的，请确认~~");
            }
        }

    }

    //修改密码
    private void updatePassword() {
        System.out.println("---修改密码界面---");
        System.out.println("请输入您当前的登录密码");
        String pw = sc.next();
        if(pw.equals(LoginAccount.getPassword())){
            while (true) {
                System.out.println("请输入您要更新的密码");
                String npw= sc.next();
                System.out.println("请再次输入您要更新的密码");
                String oknpw= sc.next();
                if(oknpw.equals(npw)){
                    LoginAccount.setPassword(oknpw);
                    System.out.println("密码修改成功，请重新登录");
                    return;
                }else{
                    System.out.println("输入的两次密码不一致，请重新输入");
                }
            }
        }else {
            System.out.println("当前密码输入错误，无法修改密码");
            return;
        }
    }

    //销户处理
    private boolean removeAccount() {
        System.out.println("---进入销户页面---");
        System.out.println("请问您确认要销户吗？y/n");
        String a = sc.next();
        if(a.equals("y")){
            //判断账户当前是否还有余额
            if(LoginAccount.getMoney()>0){
                System.out.println("您的账户当前还有余额，无法进行销户处理");
                return false;
            }else{
                accounts.remove(LoginAccount);
                return true;
            }
        }else{
            System.out.println("您的账号已保留");
            return false;
        }
    }

    //转钱
    private void transferMoney() {
        //1.判断当前列表账户数是否大于2
        if(accounts.size()<2){
            System.out.println("当前账户数量小于2个，无法进行转账");
            return;
        }
        //2.判断当前账户是否有余额
        if(LoginAccount.getMoney()<=0){
            System.out.println("您的账户没钱，别转账了吧");
            return;
        }
        //3.开始转账
        while (true) {
            System.out.println("请输入你要转账的卡号：");
            String cardId = sc.next();
            //3.1判断转账卡号是否存在
            Account acc = getAccountByCardId(cardId);
            if(acc == null){
                System.out.println("您的卡号信息输入有误，请重新输入：");
            }else{
                //3.2进行名称验证
                String name = "*"+ acc.getUsername().substring(1);
                System.out.println("请补充完整姓名信息："+name);
                String preName = sc.next();
                if(acc.getUsername().startsWith(preName)){
                    while (true) {
                        //3.3姓名验证成功，开始转账
                        System.out.println("请输入您要转账的金额：");
                        double v = sc.nextDouble();
                        //3.4判断金额是否超出当前余额
                        if(v>LoginAccount.getMoney()){
                            System.out.println("您要转的钱超出余额，您当前余额为："+LoginAccount.getMoney()+"请重新输入");
                        }else{
                            //3.5进行转账
                            LoginAccount.setMoney(LoginAccount.getMoney()-v);
                            acc.setMoney(acc.getMoney()+v);
                            System.out.println("转账成功，您当前的余额为"+LoginAccount.getMoney());
                            return;
                        }
                    }
                }
                else {
                    System.out.println("您输入的姓氏有误，请重新输入");
                }
            }
        }
    }

    //取钱
    private void drawMoney() {
        double money = LoginAccount.getMoney();
        if(money < 100){
            System.out.println("您的余额小于100，无法取款");
            return;
        }

        while (true) {
            System.out.println("请输入您要取款的金额：");
            double m = sc.nextDouble();
            if(m > money){
                System.out.println("您的余额不足，当前余额为"+money+"请重新输入");
            }else {
                if(m>LoginAccount.getLimit()){
                    System.out.println("超出您设置的取款限额，您设定的取款限额为"+LoginAccount.getLimit()+"请重新输入");
                }else{
                    LoginAccount.setMoney(money-m);
                    System.out.println("取款"+m+"您当前的余额为"+LoginAccount.getMoney());
                    break;
                }
            }
        }
    }

    // 存钱
    private void depositMoney() {
        System.out.println("---您已进入存款页面---");
        System.out.println("您当前的存款余额为："+LoginAccount.getMoney());
        System.out.println("请输入您要存款的金额");
        double money = sc.nextDouble();
        LoginAccount.setMoney(LoginAccount.getMoney()+ money);
        System.out.println("存款成功，您当前的存款余额为："+LoginAccount.getMoney());

    }

    // 展示用户信息
    private void showLoginAccount() {
        System.out.println("==当前您的账户信息如下：==");
        System.out.println("卡号：" + LoginAccount.getCardId());
        System.out.println("户主：" + LoginAccount.getUsername());
        System.out.println("性别：" + LoginAccount.getSex());
        System.out.println("余额：" + LoginAccount.getMoney());
        System.out.println("每次取现额度：" + LoginAccount.getLimit());
    }

    // 创建账户
    private void creatAccount() {
        Account acc = new Account();
        System.out.println("---开户系统 ----");

        System.out.println("请输入您的用户名");
        String name = sc.next();
        acc.setUsername(name);

        while (true) {
            System.out.println("请输入您的性别");
            char sex = sc.next().charAt(0);
            if (sex == '男' || sex == '女') {
                acc.setSex(sex);
                break;
            } else {
                System.out.println("您输入的性别有误，仅能输入男或女");
            }
        }

        while (true) {
            System.out.println("请输入您的密码");
            String password = sc.next();
            System.out.println("请确认您的密码");
            String Okpassword = sc.next();
            if (password.equals(Okpassword)) {
                acc.setPassword(password);
                break;
            } else {
                System.out.println("您输入的两次密码不一致，请重新输入");
            }
        }

        System.out.println("请输入账户每次的取款限额");
        double limit = sc.nextDouble();
        acc.setLimit(limit);

        String cardid = creatNewCardId();
        acc.setCardId(cardid);

        accounts.add(acc);

        System.out.println("恭喜您," + acc.getUsername() + "开户成功，您的卡号是"+acc.getCardId());

    }

    //  返回一个8位随机数字 - 且不能重复
    private String creatNewCardId() {
        while (true) {
            String id = "";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                int num = r.nextInt(10);
                id += num;
            }
            Account acct = getAccountByCardId(id);
            if (acct == null) {
                return id;
            }
        }
    }

    // 判断账户是否存在
    private Account getAccountByCardId(String id) {
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getCardId().equals(id)) {
                return acc;
            }
        }
        return null;//找不到
    }
}
