package member.printer;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import member.dao.MemberDao;
import member.exception.MemberNotFoundException;
import member.vo.Member;

public class MemberInfoPrinter {
	//Resource annotation의 경우 bean name과 매치되는 것을 찾는다.
	//@Resource는 생성자에는 적용불가
	//생성자가 아닌 메서드나 변수에 @Autowired, @Resource 를 사용하는 경우 디폴트 생성자가 있어야 한다
	@Resource(name="memberDao")
	private MemberDao memberDao;
	private MemberPrinter memberPrinter;
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao= memberDao;
	}
	
//	@Autowired
//	@Qualifier("sysout")
	@Resource(name="printer1")
	public void setMemberPrinter(MemberPrinter memberPrinter) {
		this.memberPrinter = memberPrinter;
		System.out.println("printer1");
	}
	
	public void printMemberInfo(String email) {
		Member member = memberDao.selectByEmail(email);
		if (member == null) {
			//System.out.println("정보 없음!\n");
			throw new MemberNotFoundException();
			// 예외 발생시 바로 try catch문에서 처리하나봄
			//return;
		}
		memberPrinter.print(member);
		System.out.println();
	}
}
