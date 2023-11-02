import dayjs from 'dayjs';

// Format lại thời gian
export const handleFormatTime = (time: string) => {
  const inputDate = dayjs(time).locale('vi'); // Đặt ngôn ngữ

  const daysOfWeek = ['Chủ Nhật', 'Thứ Hai', 'Thứ Ba', 'Thứ Tư', 'Thứ Năm', 'Thứ Sáu', 'Thứ Bảy'];

  return `${daysOfWeek[inputDate.day()]}, ${inputDate.format('DD/MM/YYYY, HH:mm [GMT]Z')}`;
};
