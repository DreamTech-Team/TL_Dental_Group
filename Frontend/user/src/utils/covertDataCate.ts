interface ListCategory1 {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
}

interface ListCategory2 {
  title: string;
  slug: string;
}

interface ListCategories {
  id: string;
  cate1Id: ListCategory1;
  cate2Id: ListCategory2;
  companyId: {
    id: string;
    name: string;
    slug: string;
  };
}

interface DataRender {
  title: string;
  slug: string;
  company: { name: string; slug: string; cate2: ListCategory2[] }[];
}

// Hàm khởi tạo danh sách chứa các Cate2
const createListCategory2 = (cate2: ListCategory2): ListCategory2 => {
  return {
    title: cate2?.title || '',
    slug: cate2?.slug || ''
  };
};

const createDataRenderItem = (listCate: ListCategories[]): DataRender => {
  const dataRenderItem: DataRender = {
    title: listCate[0].cate1Id.title,
    slug: listCate[0].cate1Id.slug,
    company: []
  };

  const companies: { name: string; slug: string; cate2: ListCategory2[] }[] = [];

  listCate.forEach((item) => {
    const existingCompany = companies.find((company) => company.name === item.companyId.name);

    if (existingCompany) {
      const existingCate2 = existingCompany.cate2.find(
        (cate2) => cate2.title === item.cate2Id.title
      );

      if (!existingCate2) {
        existingCompany.cate2.push(createListCategory2(item.cate2Id));
      }
    } else {
      companies.push({
        name: item.companyId.name,
        slug: item.companyId.slug,
        cate2: [createListCategory2(item.cate2Id)]
      });
    }
  });

  dataRenderItem.company = companies;
  return dataRenderItem;
};

const coverDataCategory = {
  handleDataRender(listDataCate: ListCategories[], dataRender: DataRender[]): DataRender[] {
    const duplicateCate1IdElements: { [id: string]: ListCategories[] } = {};

    // Lọc các phần tử có cate1Id trùng nhau
    listDataCate.forEach((item) => {
      const cate1Id = item.cate1Id.id;
      duplicateCate1IdElements[cate1Id] = duplicateCate1IdElements[cate1Id] || [];
      duplicateCate1IdElements[cate1Id].push(item);
    });

    for (const cate1Id in duplicateCate1IdElements) {
      const elements = duplicateCate1IdElements[cate1Id];

      if (elements.length > 1) {
        const dataRenderItem = createDataRenderItem(elements);
        dataRender.push(dataRenderItem);
      }
    }

    return dataRender;
  }
};

export default coverDataCategory;
