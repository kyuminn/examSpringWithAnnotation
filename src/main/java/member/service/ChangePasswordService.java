package member.service;

import javax.annotation.Resource;

import member.dao.MemberDao;
import member.exception.MemberNotFoundException;
import member.vo.Member;

public class ChangePasswordService {
	@Resource(name="memberDao")
	private MemberDao memberDao;
	
	public ChangePasswordService(MemberDao memberDao) {
		this.memberDao= memberDao;
	}
	
	public void changePassword(String email, String oldPwd, String newPwd) {
		Member member = memberDao.selectByEmail(email);
		if(member == null) {
			throw new MemberNotFoundException();
		}
		member.changePassword(oldPwd, newPwd);
		memberDao.update(member);
	}
}
