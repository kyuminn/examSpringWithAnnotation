package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;


import member.exception.AlreadyExistingMemberException;
import member.exception.IdPasswordNotMatchingException;
import member.exception.MemberNotFoundException;
import member.printer.MemberInfoPrinter;
import member.printer.MemberListPrinter;
import member.printer.VersionPrinter;
import member.request.RegisterRequest;
import member.service.ChangePasswordService;
import member.service.MemberRegisterService;

// 의존자동 주입 : @Autowired @Resource 어노테이션으로 적용 가능
// 생성자가 아닌 변수나 메서드에 어노테이션을 사용할 경우 디폴트 생성자가 있어야 함
// @Autowired는 같은 데이터 타입인 bean을 , @Resource는 같은 이름의 bean을 찾아서 자동으로 주입한다.
// @Autowired의 경우 데이터 타입이 같으면 @Qualifier 속성으로 구별한다
public class MainForSpring {
	private static ApplicationContext appctx = null;

	public static void main(String[] args) throws IOException {
		
		appctx = new GenericXmlApplicationContext("classpath:appctx.xml"); 
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			System.out.println("명령어를 입력하세요:");
			String command = reader.readLine();
			if (command.equalsIgnoreCase("exit")) {
				System.out.println("종료합니다");
				break;
			}else if(command.startsWith("new")) {
				processNewCommand(command.split(" "));
				continue;
			}else if(command.startsWith("change")) {
				processChangeCommand(command.split(" "));
				continue;
			}else if(command.equals("list")) {
				processListCommand();
				continue;
			}else if(command.startsWith("info")) {
				processInfoCommand(command.split(" "));
				continue;
			}else if(command.equals("ver")) {
				processVersionCommand();
				continue;
			}
			printHelp();
		}
	}
	

	
	private static void processNewCommand(String[] arg) {
		if (arg.length!= 5) {
			printHelp();
			return;
		}

		MemberRegisterService regSvc = appctx.getBean("memberRegSvc", MemberRegisterService.class);
		RegisterRequest req = new RegisterRequest();
		req.setEmail(arg[1]);
		req.setName(arg[2]);
		req.setPassword(arg[3]);
		req.setConfirmPassword(arg[4]);
		
		if(!req.isPasswordEqualToConfirmPassword()) {
			System.out.println("암호화 확인이 일치하지 않습니다.\n");
			return;
		}
		try {
			regSvc.regist(req);
			System.out.println("등록되었습니다.\n");
		}catch(AlreadyExistingMemberException e) {
			System.out.println("이미 존재하는 이메일입니다.\n");
		}
	}
	
	private static void processChangeCommand(String[] arg) {
		if (arg.length!=4) {
			printHelp();
			return;
		}

		ChangePasswordService changePwdSvc = appctx.getBean("changePwdSvc", ChangePasswordService.class);
		try {
			changePwdSvc.changePassword(arg[1], arg[2], arg[3]);
			System.out.println("암호가 변경되었습니다.\n");
		}catch(MemberNotFoundException e) {
			System.out.println("존재하지 않는 이메일입니다.\n");
		}catch(IdPasswordNotMatchingException e) {
			System.out.println("이메일과 암호가 일치하지 않습니다.\n");
		}
	}
	
	private static void processListCommand() {
		MemberListPrinter memberListPrinter = appctx.getBean("memberListPrinter", MemberListPrinter.class);
		memberListPrinter.printAll();
	}
	
	private static void processInfoCommand(String[] arg)  {
		if (arg.length!=2) {
			printHelp();
			return;
		}
		MemberInfoPrinter memberInfoPrinter = appctx.getBean("memberInfoPrinter", MemberInfoPrinter.class);
		try {
			memberInfoPrinter.printMemberInfo(arg[1]);
		}catch(MemberNotFoundException e) {
			System.out.println("존재하지 않는 이메일입니다.\n");
		}
			
		
	}
	
	private static void processVersionCommand() {
		VersionPrinter versionPrinter = appctx.getBean("versionPrinter", VersionPrinter.class);
		versionPrinter.printVersion();
	}
	private static void printHelp() {
		System.out.println();
		System.out.println("명령어 사용법");
		System.out.println("new [이메일] [이름] [암호] [암호확인]");
		System.out.println("change [이메일] [현재비밀번호] [변경할비밀번호]");
		System.out.println("list");
		System.out.println("info [이메일]");
		System.out.println("ver");
		System.out.println();
	}
}
