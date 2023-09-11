import java.io.*;
import java.util.*;

/*
state:
000:错误字段
001:加法运算符
002:减法运算符
003:乘法运算符
004:除法运算符
005:左括号
006:右括号
007:字符串
008.标识符
009.保留字
010.整数
011.浮点数
012.等号运算符
013.赋值运算符
014:大于号
015:小于号
016：不等于号
017：小于等于号
018：大于等于号
019：[ 左中括号
020：] 右中括号
021: ; 分号
022: { 左大括号
023: } 右大括号
024：.
025: "
026: #
*/
public class compiler {
    private static char[] Code;//转化为字符数组
    private static int len;//字符数组长度
    private static String str="";//字符数组->String，即 操作字符串

    public static ArrayList<String> outPut=new ArrayList<>();

    public static void main(String[] args) throws IOException {
        read();
        //str="\"ab\"#";
        StartLexicalAnalysis(str);

        write();
    }
    private static class TwoTuple{
        public static String type;
        public static String value;

        public TwoTuple(String type,String value) {
            this.type=type;
            this.value=value;
        }

        @Override
        public String toString() {
            return "("+this.type+","+this.value+")";
        }
    }

    public static void StartLexicalAnalysis(String s) {//开始进行语法分析
        if (IsEmpty(s)) {
            if(IsValid(s)){
                //tokens.add("begin") ;
                System.out.println("当前输入符号为：" + s.substring(0,s.length()));// -1
                System.out.println("词法分析结果如下：");
                StartState(0, 0);
            }else{
                System.out.println("当前输入符号为：" + s.substring(0,s.length()));// -1
            }
        }
    }
    private static boolean IsEmpty(String s){
        if (s.isEmpty()||s.toCharArray()[0]=='#') {
            System.out.println("源代码为空，无法进行词法分析！");
            return false;
        }
        else {
            Code = s.toCharArray();
            len=s.length();
            return true;
        }
    }

    private static boolean IsValid(String s){
        if (Code[len-1]!='#') {
            System.err.println("输入串有误，无法进行词法分析！");
            return false;
        }
        else {
            return true;
        }
    }

    private static void read() throws IOException{
        File file = new File("../in.txt");
        BufferedReader br=new BufferedReader(new FileReader(file));
        String s ="" ;
        while((s=br.readLine())!=null){
            str+=s;
        }
        br.close();
    }

    private static void write() throws IOException{
        File file = new File("../debug.txt");
        BufferedWriter bw=new BufferedWriter(new FileWriter(file));
        if(outPut.size()!=0){
            bw.write(String.valueOf(outPut));
            System.out.println();
            bw.close();
        }


    }

    private static void StartState(int startPointer, int currentPointer)//初始状态
    {

        if (Code[currentPointer] == '#') {
            JHState(currentPointer);
            System.out.println("词法分析结束");

        }
        else if (Code[currentPointer] == ' ') {
            StartState(startPointer+1,currentPointer+1);//如果当前字符是空格 后移
        }
        else if(Code[currentPointer] == '\n'){
            StartState(startPointer+1,currentPointer+1);//如果当前字符是换行符 后移
        }
        else if (IsEqual(Code[currentPointer]))//如果当前字符是=进入EqualState
        {
            EqualState(startPointer,currentPointer);
        }
        else if (IsAdd(Code[currentPointer]))//如果当前字符是+进入AddState
        {
            AddState(currentPointer);
        }
        else if (IsSub(Code[currentPointer]))//如果当前字符是-进入SubState
        {
            SubState(currentPointer);
        }
        else if (IsMul(Code[currentPointer]))//如果当前字符是*进入MulState
        {
            MulState(currentPointer);
        }
        else if (IsDiv(Code[currentPointer]))//如果当前字符是/进入DivState
        {
            DivState(currentPointer);
        }
        else if (IsLParent(Code[currentPointer]))//如果当前字符是(进入LParentState
        {
            LParentState(currentPointer);
        }
        else if (IsRParent(Code[currentPointer]))//如果当前字符是)进入RParentState
        {
            RParentState(currentPointer);
        }

        //判断保留字
        else if (IsI(Code[currentPointer]))//如果当前字符是i进入if_State
        {
            if_State(startPointer, currentPointer);
        }
        else if(IsI(Code[currentPointer])){//如果当前字符是i进入int_State
            int_State(startPointer, currentPointer);
        }
        else if (IsE(Code[currentPointer]))//如果当前字符是e进入else_State
        {
            else_State(startPointer, currentPointer);
        }
        else if (IsS(Code[currentPointer]))//如果当前字符是s进入String_State
        {
            String_State(startPointer, currentPointer);
        }
        else if (IsS(Code[currentPointer]))//如果当前字符是s进入start_State
        {
            start_State(startPointer, currentPointer);
        }

        //******

        else if (IsAlpha(Code[currentPointer]))//如果当前字符是字母进入IdentState
        {
            IdentState(startPointer, currentPointer );
        }
        else if (IsDigit(Code[currentPointer]))//如果当前字符是数字进入IntState
        {
            IntState(startPointer, currentPointer );
        }
        else if ( IsGreater(Code[currentPointer]) )
        { //如果当前字符是>
            GreaterState(startPointer, currentPointer);
        }
        else if ( IsLess(Code[currentPointer]) )
        { //如果当前字符是<
            LessState(startPointer, currentPointer);
        }
        else if ( IsExc(Code[currentPointer]) )
        { //如果当前字符是！
            ExcState(startPointer, currentPointer);
        }
        else if ( IsLZKH(Code[currentPointer]) )
        { //如果当前字符是[
            LZKHState(startPointer, currentPointer);
        }
        else if ( IsRZKH(Code[currentPointer]) )
        { //如果当前字符是]
            RZKHState(startPointer, currentPointer);
        }
        else if ( IsLDKH(Code[currentPointer]) )
        { //如果当前字符是{
            LDKHState(startPointer, currentPointer);
        }
        else if ( IsRDKH(Code[currentPointer]) )
        { //如果当前字符是}
            RDKHState(startPointer, currentPointer);
        }
        else if ( IsFH(Code[currentPointer]) )
        { //如果当前字符是;
            FHState(startPointer, currentPointer);
        }
        else if ( IsD(Code[currentPointer]) )
        { //如果当前字符是.
            DState(startPointer, currentPointer);
        }
        else if ( IsDH(Code[currentPointer]) )
        { //如果当前字符是,
            DHState(startPointer, currentPointer);
        }
        else if ( IsYH(Code[currentPointer]) )
        { //如果当前字符是"
            StrState(startPointer, currentPointer);
        }
        else {
            System.out.println("(000,错误," + Code[currentPointer] + ")");
            String tempStr="\n"+"(000,错误," + Code[currentPointer] + ")   ";
            outPut.add(tempStr);
            StartState(startPointer + 1, currentPointer + 1);
        }
    }

    private static void if_State(int i, int j){
        if(Code[j+1]=='f'){
            if(IsDigit(Code[j+2] )|| IsAlpha(Code[j+2])) {
                IdentState(i, j);
            }else{
                System.out.print("(009,保留字,");
                String tempStr="\n"+"(009,保留字，";
                printA(i, j+2, tempStr);
            }
        }else{
            IdentState(i, j);
        }
    }

    private static void else_State(int i, int j){
        if(Code[j+1]=='l'){
            if(Code[j+2]=='s'){
                if(Code[j+3]=='e'){
                    if(IsDigit(Code[j+4] )|| IsAlpha(Code[j+4])) {
                        IdentState(i, j);
                    }else{
                        System.out.print("(009,保留字,");
                        String tempStr="\n"+"(009,保留字,";
                        printA(i, j+4, tempStr);
                    }
                }else{
                    IdentState(i, j);
                }
            }else{
                IdentState(i, j);
            }
        }else{
            IdentState(i, j);
        }
    }

    private static void String_State(int i, int j){
        if(Code[j+1]=='t'){
            if(Code[j+2]=='r'){
                if(Code[j+3]=='i'){
                    if(Code[j+4]=='n'){
                        if(Code[j+5]=='g'){
                            if(IsDigit(Code[j+6] )|| IsAlpha(Code[j+6])) {
                                IdentState(i, j);
                            }else{
                                System.out.print("(009,保留字,");
                                String tempStr="\n"+"(009,保留字,";
                                printA(i, j+6, tempStr);
                            }
                        }else{
                            IdentState(i, j);
                        }
                    }else{
                        IdentState(i, j);
                    }
                }else{
                    IdentState(i, j);
                }
            }else{
                IdentState(i, j);
            }
        }else{
            IdentState(i, j);
        }
    }

    private static void start_State(int i, int j){
        if(Code[j+1]=='t'){
            if(Code[j+2]=='a'){
                if(Code[j+3]=='r'){
                    if(Code[j+4]=='t'){
                        if(IsDigit(Code[j+5] )|| IsAlpha(Code[j+5])) {
                            IdentState(i, j);
                        }else{
                            System.out.print("(009,保留字,");
                            String tempStr="\n"+"(009,保留字,";
                            printA(i, j+5, tempStr);
                        }
                    }else{
                        IdentState(i, j);
                    }
                }else{
                    IdentState(i, j);
                }
            }else{
                IdentState(i, j);
            }
        }else{
            IdentState(i, j);
        }
    }

    private static void int_State(int i, int j){
        if(Code[j+1]=='n'){
            if(Code[j+2]=='t'){
                if(IsDigit(Code[j+3] )|| IsAlpha(Code[j+3])) {
                    IdentState(i, j);
                }else{
                    System.out.print("(009,保留字,");
                    String tempStr="\n"+"(009,保留字,";
                    printA(i, j+3, tempStr);
                }
            }else{
                IdentState(i, j);
            }
        }else{
            IdentState(i, j);
        }
    }

    private static void JHState(int j)//表示字符为加法运算符
    {
        System.out.println("(026,#," + Code[j] + ")");
        String tempStr="\n"+"(026,#," + Code[j] + ")"+"\n";
        outPut.add(tempStr);
    }
    private static void DHState(int startPointer, int j) {
        System.out.println("(025,逗号," + Code[j] + ")");
        String tempStr="\n"+"(025,逗号," + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }
    private static void DState(int startPointer, int j) {
        System.out.println("(024,点," + Code[j] + ")");
        String tempStr="\n"+"(024,点," + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void FHState(int startPointer, int j) {
        System.out.println("(021,分号," + Code[j] + ")");
        String tempStr="\n"+"(021,分号," + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void LZKHState(int startPointer, int j) {
        System.out.println("(019,左中括号," + Code[j] + ")");
        String tempStr="\n"+"(019,左中括号," + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void RZKHState(int startPointer, int j) {
        System.out.println("(020,右中括号," + Code[j] + ")");
        String tempStr="\n"+"(020,右中括号," + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void LDKHState(int startPointer, int j) {
        System.out.println("(022,左大括号," + Code[j] + ")");
        String tempStr="\n"+"(022,左大括号," + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void RDKHState(int startPointer, int j) {
        System.out.println("(023,右大括号," + Code[j] + ")");
        String tempStr="\n"+"(023,右大括号," + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void ExcState(int startPointer, int j) {
        if ( Code[j+1] == '=' ) {
            ExcState(startPointer,j+1);
        }else {
            if ( j - startPointer == 1 ) {
                System.out.print("(016,不等于号,");
                String tempStr = "\n"+"(016,不等于号," ;
                printA(startPointer,j+1,tempStr);
            }else {
                System.out.print("(000,错误," );
                String tempStr="\n"+"(000,错误,";
                printA(startPointer,j+1,tempStr);
            }
        }
    }

    private static void LessState(int startPointer, int j) {
        if ( Code[j+1] == '=' ) {
            LessState(startPointer,j+1);
        }else {
            if ( j - startPointer == 1 ) {
                System.out.print("(017,小于等于号,");
                String tempStr = "\n"+"(017，小于等于号," ;
                printA(startPointer,j+1,tempStr);
            }else if (j - startPointer == 0) {
                System.out.print("015,小于号," + Code[j] + ")");
                String tempStr="\n"+"(015，小于号，" + Code[j] + ")   ";
                outPut.add(tempStr);
                StartState(j + 1, j + 1);
            }
            else {
                System.out.print("(000,错误," );
                String tempStr="\n"+"(000,错误,";
                printA(startPointer,j+1,tempStr);
            }
        }
    }

    private static void GreaterState(int startPointer, int j) {
        if ( Code[j+1] == '=' ) {
            GreaterState(startPointer,j+1);
        }else {
            if ( j - startPointer == 1 ) {
                System.out.print("(018,大于等于号,");
                String tempSt = "\n"+"(018,大于等于号," ;
                printA(startPointer,j+1,tempSt);
            }else if (j - startPointer == 0) {
                System.out.println("(014,大于号," + Code[j] + ")");
                String tempStr="\n"+"(014，大于号，" + Code[j] + ")   ";
                outPut.add(tempStr);
                StartState(j+1 , j + 1);
            }
            else {
                System.out.print("(000,错误," );
                String tempSt="\n"+"(000,错误,";
                printA(startPointer,j+1,tempSt);
            }
        }
    }

    private static void EqualState(int startPointer ,int j )//表示字符为等号运算符
    {
        if ( IsEqual(Code[ j+1 ]) ) {
            EqualState( startPointer,j+1);
        }else {
            if ( j - startPointer == 1 ) {// ==
                System.out.print("(012，等于号，");
                String tempSt="\n"+"(012,等于号," ;
                printA(startPointer,j+1,tempSt);
            }else if (j - startPointer == 0) {//=
                System.out.println("(013，赋值运算符," + Code[j] + ")");
                String tempStr="\n"+"(013，赋值运算符,"+Code[j];
                outPut.add(tempStr);
                StartState(j + 1, j + 1);
            }else {//错误代码
                System.out.print("(000,错误," );
                String tempStr="\n"+"(000,错误,";
                printA(startPointer,j+1,tempStr);
            }
        }

    }

    private static void AddState(int j)//表示字符为加法运算符
    {
        System.out.println("(001，加号，" + Code[j] + ")");
        String tempStr="\n"+"(025，逗号，" + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void SubState(int j)//表示字符为减法运算符
    {
        System.out.println("(002，减号，" + Code[j] + ")");
        String tempStr="\n"+"(002，减号，" + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void MulState(int j)//表示字符为乘法运算符
    {
        System.out.println("(003，乘号，" + Code[j] + ")");
        String tempStr="\n"+"(003，乘号，" + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void DivState(int j)//表示字符为除法运算符
    {
        System.out.println("(004，除号，" + Code[j] + ")");
        String tempStr="\n"+"(004，除号，" + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void LParentState(int j)//字符为左括号
    {
        System.out.println("(005，左括号，" + Code[j] + ")");
        String tempStr="\n"+"(005，左括号，" + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void RParentState(int j)//字符为右括号
    {
        System.out.println("(006，右括号，" + Code[j] + ")");
        String tempStr="\n"+"(006，右括号，" + Code[j] + ")   ";
        outPut.add(tempStr);
        StartState(j + 1, j + 1);
    }

    private static void StrState(int i, int j){
        while (!IsYH(Code[j+1])){
            if(IsEND(Code[j+1])){
                System.out.print("(000,错误," );
                String tempStr="\n"+"(000,错误,";
                printA(i,j+1,tempStr);
                break;
            }
            j++;
            StrState(i,j);
            return;



        }
        if(IsYH(Code[j+1])){
            System.out.print("(007,字符串,");
            String tempStr="\n"+"(007,字符串,";
            printA(i, j+2, tempStr);
        }
    }


    private static void IdentState(int i, int j)//标识符
    {
        if (IsDigit(Code[j+1]) || IsAlpha(Code[j+1]))
        //如果当前字符仍然为字母或数字则再次进入IdentState
        {
            IdentState(i, j + 1);
        } else//如果当前字符为非数字及字母字符，则表明Code[i]~Code[j]这一段是标识符
        {
            System.out.print("(008,标识符,");
            String tempStr="\n"+"(008,标识符,";
            printA(i, j+1, tempStr);
        }
    }

    private static void IntState(int i, int j)//准实数
    {
        if (IsDigit(Code[j+1]))//如果当前字符仍然是数字，则再次进入IntState
        {
            IntState(i, j + 1);
        }
        if (Code[j+1] == '.')//如果当前字符是小数点，进入PointState
        {
            PointState(i, j + 1);
        }
        if ((Code[j+1] != '.') && !IsDigit(Code[j+1]))
        //如果当前字符既不为小数点也不是数字 ，则表明Code[i]~Code[j-1]这一段是个实数
        {
            System.out.print("(010,整数,");
            String tempStr="\n"+"(010,整数,";
            printA(i, j+1, tempStr);
        }
    }

    private static void PointState(int i, int j)//整数后接个小数点的中间态
    {
        //Code[i]~Code[j-1]之中含有小数点
        if (!IsDigit(Code[j])) {//小数点后还是小数点或者不是数字时报错
            System.out.print("(000,错误,");
            String tempStr="\n"+"(000,错误,";
            int q = i;//从Code[i]开始算起
            while ((IsDigit(Code[q])) || (Code[q]) == '.')
            //直接相连的数字或小数点都属于这个无效字段的一部分
            {
                tempStr=tempStr+Code[q];
                System.out.print(Code[q]);
                q++;
            }
            System.out.println(")");
            tempStr=tempStr+ ")   ";
            outPut.add(tempStr);
            //Code[q]此时为无效字段的下一个字符
            StartState(q, q);

        }
        if (IsDigit(Code[j]))//如果当前字符是数字，则进入FloatState
        {
            FloatState(i, j + 1);
        }
    }

    private  static void FloatState(int i,int j){

        if (IsDigit(Code[j]))//如果当前字符是数字，则再次进入FloatState
        {
            FloatState(i, j + 1);
        }
        if (!IsDigit(Code[j]))
        //如果当前字符是非数字字符，说明Code[i]~Code[j-1]这一段是浮点数
        {
            System.out.print("(011,浮点数,");
            String tempStr="\n"+"(011,浮点数,";
            printA(i, j, tempStr);
        }
    }

    private static void printA(int i, int j, String tempStr) {//用于输出非单一字符
        for (int k = i; k < j; k++) {
            tempStr = tempStr + Code[k];
            System.out.print(Code[k]);
        }
        tempStr=tempStr+ ")   ";
        System.out.println(")");
        outPut.add(tempStr);
        StartState(j, j);
    }

    //***************************
    //当前读头下字符
    private static boolean IsI(char ch){
        return (ch=='i')||(ch=='I');
    }

    private static boolean IsS(char ch){
        return (ch=='s')||(ch=='S');
    }

    private static boolean IsE(char ch){
        return (ch=='e')||(ch=='E');
    }

    private static boolean IsEqual(char ch) {//判断是否是字符'='
        return ch == '=';
    }

    private static boolean IsAdd(char ch)//判断是否是字符'+'
    {
        return ch == '+';
    }

    private static boolean IsSub(char ch)//判断是否是字符'-'
    {
        return ch == '-';
    }

    private static boolean IsMul(char ch)//判断是否是字符'*'
    {
        return ch == '*';
    }

    private static boolean IsDiv(char ch)//判断是否是字符'/'
    {
        return ch == '/';
    }

    private static boolean IsLParent(char ch)//判断是否左括号
    {
        return ch == '(';
    }

    private static boolean IsRParent(char ch)//判断是否是有括号
    {
        return ch == ')';
    }


    private static boolean IsDigit(char ch) {//判断是否是数字
        return Character.isDigit(ch);
    }

    private static boolean IsAlpha(char ch) {//判断是否是字母
        return Character.isLetter(ch);
    }

    private static boolean IsExc(char ch) {
        return ch == '!';
    }

    private static boolean IsLess(char ch) {
        return ch == '<';
    }

    private static boolean IsGreater(char ch) {
        return ch == '>';
    }

    private static boolean IsFH(char ch) {
        return ch == ';';
    }

    private static boolean IsD(char ch) {
        return ch == '.';
    }
    private static boolean IsDH(char ch) {
        return ch == ',';
    }
    private static boolean IsYH(char ch) {
        return ch == '"';
    }
    private static boolean IsRZKH(char ch) {
        return ch == ']';
    }

    private static boolean IsLZKH(char ch) {
        return ch == '[';
    }

    private static boolean IsRDKH(char ch) {
        return ch == '}';
    }

    private static boolean IsLDKH(char ch) {
        return ch == '{';
    }

    private static boolean IsEND(char ch) {
        return ch == '#';
    }
}
