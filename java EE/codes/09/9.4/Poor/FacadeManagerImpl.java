

/**
 * Description:
 * <br/>��վ: <a href="http://www.crazyit.org">���Java����</a> 
 * <br/>Copyright (C), 2001-2012, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class FacadeManagerImpl
	implements FacadeManager
{
	//ҵ���߼�����������DAO����
	private CategoryDao categoryDao;
	private NewsDao newsDao;
	private NewsReviewDao newsReviewDao;
	private UserDao userDao;

	//�˴���Ӧ����������ע��DAO��������setter����
	...
	//�˴���Ӧ����������ҵ���߼�����
	...
	//�������������Żظ���ҵ�񷽷�
	public NewsReview addNewsReview(Long newsId , String content)
	{
		//��������id��������
		News news = newsDao.getNews(newsId);
		//��Ĭ�Ϲ������������Żظ�
		NewsReview review = new NewsReview();
		//�������������Żظ�֮��Ĺ���
		review.setNews(news);
		//�������Żظ�������
		review.setContent(content);
		//���ûظ��Ļظ�ʱ��
		review.setPostDate(new Date());
		//�������Żظ�������޸�ʱ��
		review.setLastModifyDate(new Date());
		//����ظ�
		newsReviewDao.saveNewsReview(review);
		return review;
	}
}
