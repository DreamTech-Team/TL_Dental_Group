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

const coverDataCategory = {
  // handleDataCate(
  //   listCategory1: ListCategory1[],
  //   listCategory2: ListCategory2[],
  //   dataRender: DataRender[]
  // ): DataRender[] {
  //   const uniqueElementsSet = new Set<string>();
  //   const duplicatesPositions: { [key: string]: number[] } = {};
  //   const duplicatesPositions1: { [key: string]: number[] } = {};

  //   listCategory1.forEach((item, index) => {
  //     const { id } = item;

  //     if (uniqueElementsSet.has(id)) {
  //       if (!duplicatesPositions[id]) {
  //         duplicatesPositions[id] = [index];
  //       } else {
  //         duplicatesPositions[id].push(index);
  //       }
  //     } else {
  //       uniqueElementsSet.add(id);
  //       duplicatesPositions1[id] = [index];
  //       duplicatesPositions1[id].push(index);
  //     }
  //   });

  //   Object.keys(duplicatesPositions).forEach((id) => {
  //     duplicatesPositions[id].push(duplicatesPositions1[id][0]);
  //     delete duplicatesPositions1[id];
  //   });

  //   const uniqueElementsArray = Array.from(uniqueElementsSet).map((id) => {
  //     return listCategory1.find((item) => item.id === id);
  //   });

  //   uniqueElementsArray.forEach((item) => {
  //     if (item) {
  //       const object = {
  //         title: '',
  //         slug: '',
  //         data: [
  //           {
  //             name: '',
  //             slug: ''
  //           }
  //         ]
  //       };
  //       const dataCate2: { name: string; slug: string }[] = [];

  //       if (duplicatesPositions1[item.id]) {
  //         object.title = item.title;
  //         object.slug = item.slug;

  //         if (listCategory2[duplicatesPositions1[item.id][0]] !== null) {
  //           dataCate2.push({
  //             name: listCategory2[duplicatesPositions1[item.id][0]].title,
  //             slug: listCategory2[duplicatesPositions1[item.id][0]].slug
  //           });
  //         }

  //         object.data = dataCate2;

  //         dataRender.push(object);
  //       }
  //       if (duplicatesPositions[item.id]) {
  //         object.title = item.title;
  //         object.slug = item.slug;

  //         duplicatesPositions[item.id].forEach((replace) => {
  //           if (listCategory2[replace] !== null) {
  //             dataCate2.push({
  //               name: listCategory2[replace].title,
  //               slug: listCategory2[replace].slug
  //             });
  //           }
  //         });

  //         object.data = dataCate2;

  //         dataRender.push(object);
  //       }
  //     }
  //   });

  //   return dataRender;
  // },
  handleDataRender(listDataCate: ListCategories[], dataRender: DataRender[]) {
    // Đối tượng để lưu trữ các phần tử có cate1Id trùng nhau
    const duplicateCate1IdElements: { [id: string]: ListCategories[] } = {};

    // Lặp qua mảng để lọc ra các phần tử có cate1Id trùng nhau
    if (listDataCate) {
      for (const item of listDataCate) {
        const cate1Id = item.cate1Id.id;
        if (!duplicateCate1IdElements[cate1Id]) {
          duplicateCate1IdElements[cate1Id] = [];
        }
        duplicateCate1IdElements[cate1Id].push(item);
      }
    }

    for (const item in duplicateCate1IdElements) {
      if (duplicateCate1IdElements[item].length > 1) {
        const object = {
          title: '',
          slug: '',
          company: [
            {
              name: '',
              slug: '',
              cate2: [
                {
                  title: '',
                  slug: ''
                }
              ]
            }
          ]
        };

        const companys: {
          name: string;
          slug: string;
          cate2: { title: string; slug: string }[];
        }[] = [];

        duplicateCate1IdElements[item].forEach((item1) => {
          object.title = item1.cate1Id.title;
          object.slug = item1.cate1Id.slug;

          const existingCompany = companys.find((company) => company.name === item1.companyId.name);

          if (existingCompany) {
            const existingCate2 = existingCompany.cate2.find(
              (cate2) => cate2.title === item1.cate2Id.title
            );

            if (!existingCate2) {
              existingCompany.cate2.push({
                title: item1.cate2Id.title || '',
                slug: item1.cate2Id.slug || ''
              });
            }
          } else {
            companys.push({
              name: item1.companyId.name,
              slug: item1.companyId.slug,
              cate2: [
                {
                  title: item1.cate2Id && item1.cate2Id.title !== null ? item1.cate2Id.title : '',
                  slug: item1.cate2Id && item1.cate2Id.slug !== null ? item1.cate2Id.slug : ''
                }
              ]
            });
          }

          object.company = companys;
        });

        dataRender.push(object);
      }
    }

    return dataRender;
  }
};

export default coverDataCategory;
