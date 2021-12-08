package member.printer;

import java.util.Collection;

import javax.annotation.Resource;

import member.dao.MemberDao;
import member.vo.Member;

public class MemberListPrinter {
	@Resource(name="memberDao")
	private MemberDao memberDao;
	@Resource(name="printer1")
	private MemberPrinter memberPrinter;
	
	//@Autowired
	public MemberListPrinter(MemberDao memberDao,/*@Qualifier("sysout")*/MemberPrinter memberPrinter) {
		this.memberDao= memberDao;
		this.memberPrinter = memberPrinter;
	}
	public MemberListPrinter() {
		/*
		 *@Resource를 통한 의존 자동 주입은 생성자에 설정할 수 없으므로 디폴트 생성자가 있어야 에외가 발생하지 않는다
		 * */
	}
	
	public void printAll() {
		Collection<Member> members = memberDao.selectAll();
		for(Member m : members) {
			memberPrinter.print(m);
		}
	}
}
