import daiNgo from '@/assets/icons/ic_DaiNgo.png';
import light from '@/assets/icons/ic_light.png';
import clock from '@/assets/icons/ic_clock.png';

const timeWork = `<ul> <li> <span>Thứ 2 - 6:</span> <span>08:00 - 17:00</span></li>
<li> <span>Thứ 2 - 6:</span> <span>08:00 - 17:00</span></li>
<li> <span>Thứ 2 - 6:</span> <span>08:00 - 17:00</span></li> </ul>`;

export const items = [
  {
    icon: { link: daiNgo, color: 'color-icon1' },
    title: 'ĐÃI NGỘ HẤP DẪN',
    content:
      'Mức thu nhập của nhân sự khi làm việc tại TL Dental' +
      'Group rất hấp dẫn dựa trên năng lực và vị trí công việc.' +
      ' Ngoài thu nhập, bạn hưởng đầy đủ các chế độ BHXH và chế độ khác theo quy định.'
  },
  {
    icon: { link: light, color: 'color-icon2' },
    title: 'TỰ DO VÀ SÁNG TẠO',
    content:
      'Nhân viên tại TL Dental Group sẽ được trải nghiệm văn hóa làm việc' +
      ' chủ động, sáng tạo và không kém phần thoải mái, điều đó sẽ thúc đẩy bạn hoàn ' +
      'thành mọi nhiệm vụ và đạt kết quả mong muốn.'
  },
  {
    icon: { link: clock, color: 'color-icon3' },
    title: 'GIỜ LÀM VIỆC',
    content: timeWork
  }
];
