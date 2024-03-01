/*<![CDATA[*/
    window.onload = () => {
        renderPostInfo();
    }

    // 게시글 정보 렌더링
    function renderPostInfo() {
        const post = [[ ${post} ]];

        if(!post) {
            initCreatedDate();
            return false;
        }

        const form = document.getElementById('saveForm');
        const fields = ['id', 'category', 'title', 'content', 'writer', 'publicYn'];
        form.publicYn.checked = post.publicYn;

        document.getElementById('createdDate').textContent = post['createdDate'];

        fields.forEach(field => {
            form[field].value = post[field];
        })
    }


    // 등록일 초기화
    function initCreatedDate() {
        document.getElementById('createdDate').textContent = dayjs().format('YYYY-MM-DD');
    }


    // 게시글 저장
    function savePost() {
        const form = document.getElementById('saveForm');
        form.publicYn.value = form.publicYn.checked;
        const fields = [form.category, form.title, form.writer, form.content];
        const fieldNames = ['카테고리', '제목', '작성자', '내용'];

        for(let i = 0, len = fields.length; i < len; i++) {
            isValid(fields[i], fieldNames[i]);
        }

        document.getElementById('saveBtn').disabled = true;
        form.action = [[ ${post == null} ]] ? '/post/save' : '/post/update';
        form.submit();

    }
/*]]>*/