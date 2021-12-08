package member.service;

import java.util.Date;

import javax.annotation.Resource;

import member.dao.MemberDao;
import member.exception.AlreadyExistingMemberException;
import member.request.RegisterRequest;
import member.vo.Member;

public class MemberRegisterService {
	@Resource(name="memberDao")
	private MemberDao memberDao;
	
	
	public MemberRegisterService(MemberDao memberDao) {
		this.memberDao= memberDao;
	}
	
	public void regist(RegisterRequest req) {
		Member member = memberDao.selectByEmail(req.getEmail());
		if (member!=null) {
			throw new AlreadyExistingMemberException("dup email"+req.getEmail());
		}
		Member newMember = new Member(req.getEmail(), req.getPassword(), req.getName(), new Date());
		memberDao.insert(newMember);
	}
}
