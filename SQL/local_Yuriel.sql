-- SQL Developer에서는 주석이 hyphen x 2

SELECT TO_date ('10 9월 1992', 'DD MONTH YYYY') FROM DUAL;
SELECT TRUNC(SYSDATE, 'YEAR') FROM DUAL;

-- Q. 사원테이블(S_EMP)의 모든 데이터를 출력하라.
SELECT
    *
FROM
    s_emp; -- Ctrl+Enter 치면 커서 있는 명령문 실행 (현재 노랗게 하이라이트 된 부분)
                     -- F5은 조심해야 한다!! 모든 명령문을 자동으로 실행하기 때문에, 원하지 않는 명령문도 잘못 실행될 수 있다!
                     -- 한 번 commit한 것은 rollback할 수 없다!!

-- Q. 박구곤 사장의 직위(TITLE)을 사원으로 변경하라.

UPDATE s_emp
SET
    title = '사원'
WHERE
    id = 1; -- 여러 행에 분리시킨 이유는, 일부만 변경하여 실행할 수도 있기 때문이다 --> semi-colon도 따로 분리시킨 이유는, 일부를 제거해도 semi-colon은 영향 안 받게 하기 위해서이다
  -- Ctrl + Slash 하면 주석처리 된다

DELETE FROM s_emp;

SELECT
    name   이름,
    start_date,
    salary * 18 연봉
FROM
    s_emp; -- name 대신에 별명 "이름"을 적용하고, salary*18 대신 별명 "연봉"을 적용한 것이다

SELECT
    name,
    title
FROM
    s_emp;

SELECT
    name
    || ' '
    || title
FROM
    s_emp;

-- Q. 직원들의 직책(title)을 중복없이 출력하라.

SELECT DISTINCT
    title
FROM
    s_emp;
--Select distinct title, name from s_emp; --> 이름과 직책이 둘 다 같은 경우만 중복으로 본다!

SELECT DISTINCT
    title
FROM
    s_emp
ORDER BY
    title DESC;

-- Q. 직책(title)이 사원인 사람만 출력하라.

SELECT
    *
FROM
    s_emp
WHERE
    title = '사원'
    AND salary BETWEEN 1000 AND 2000 -- between을 사용하면 경계값도 포함된다 --> 1000 <= salary <= 2000
    ;

-- Q. 직원들의 모든 부서를 중복없이 출력하고 부서를 오름차순으로 정렬하라.

SELECT DISTINCT
    dept_id
FROM
    s_emp
ORDER BY
    1;

-- Q. 부서 101, 102에 속한 직원들을 출력하라.

SELECT
    *
FROM
    s_emp
WHERE
    dept_id IN (
        101,
        102
    )
ORDER BY
    dept_id;

-- Q. 

SELECT
    title,
    length(title),
    lengthb(title)
FROM
    s_emp;

SELECT
    add_months(SYSDATE, - 2)
FROM
    dual;

SELECT
    round(SYSDATE, 'year')
FROM
    dual;

SELECT
    trunc(SYSDATE, 'month')
FROM
    dual;

SELECT
    TO_CHAR(SYSDATE + 4, 'dd / month, year')
FROM
    dual; -- sysdate은 숫자로 저장되는데, 그것을 to_char을 이용해서 문자열로 바꾼다

-- Q. 전체 직원이 몇 명인지 세어라.

SELECT
    COUNT(*)
FROM
    s_emp;

-- Q. 전체 직원의 평균 연봉을 출력하라.

SELECT
    AVG(salary) AS average_salary
FROM
    s_emp;

SELECT
    dept_id,
    round(AVG(salary), 2)
FROM
    s_emp
GROUP BY
    dept_id
ORDER BY
    2 DESC -- Select절 내용, dept_id & round(avg(salary),2) 중 2번째를 기준으로!
                -- BUT Select절의 내용이나 그 순서가 바뀔 수 있음으로 숫자 대신 제대로 컬럼명을 작성하는 것이 좋다!
    ;

SELECT
    dept_id,
    name,
    salary
FROM
    s_emp
ORDER BY
    1
--Group by dept_id -- dept_id가 중복되는 사람들은 하나의 dept_id 그룹에 담는다
                   -- 에러난다! 왜냐하면 name은 개별적인 데이터인데 이것을 하나의 그룹으로 묶을 수 없기 때문이다 --> i.e. dept_id 113에는 5명이 있는데, 그룹으로 묶으면 누구 이름이 와야하나?
                   -- "Group by"를 사용하려면 Select절에 그룹함수 OR 그룹인 것이 와야한다 --> name처럼 그룹이 아닌 것은 올 수 없다
    ;

-- Q. 부서별 사원 수를 출력하라.

SELECT
    dept_id,
    COUNT(*)
FROM
    s_emp --> 전체 사원 수
GROUP BY
    dept_id
ORDER BY
    1;

-- Q. 각 지역(region_id)별로 몇 개의 부서가 있는지 출력하라.

SELECT
    region_id,
    COUNT(*)
FROM
    s_dept
GROUP BY
    region_id
ORDER BY
    1;

-- Q. 각 부서(dept_id)별로 평균급여를 구하되 급여가 2000 이상인 부서만 출력하라.

SELECT
    dept_id,
    round(AVG(salary), 2) 평균급여
FROM
    s_emp
GROUP BY
    dept_id
HAVING
    AVG(salary) >= 2000 -- 그룹함수에 대한 조건은 "where"가 아니라 "having"을 사용해서 나타낸다
                           -- Having절이 Group by 절 뒤에 와야 한다
ORDER BY
    2 DESC;

-- Having절 - 예제 7
-- Q. title별로 salary의 총합을 구하되, '부장'인 사람은 제외하고, sum(salary)가 8000 이상인 title만 나타내고, sum(salary)를 오름차순 하라.

SELECT
    title,
    SUM(salary)
FROM
    s_emp
WHERE
    title NOT LIKE '%부장' -- '%' 정규식의 '*'과 같다 --> 앞(뒤)에 0개 이상 다른 문자가 붙은 것도 '부장'과 같이 본다 --> ex) '%부장'으로 하면 부장, 인사부장, 기획부장 등을 모두 잡는다
GROUP BY
    title
HAVING
    SUM(salary) >= 8000 -- 그룹함수의 조건이니까 Having절로 작성
ORDER BY
    2 DESC;

-- Q. dept_id별로 title이 '사원'인 경우에만 avg(salary)를 구하라.

SELECT
    dept_id,
    AVG(salary)
FROM
    s_emp
WHERE
    title = '사원' -- 그룹함수의 조건이 아니니까 Where절로 작성
GROUP BY
    title,
    dept_id
ORDER BY
    2 DESC --> 정렬은 항상 마지막에 (다른 작업을 마친 후 하도록)
    ;

-- Group을 Subgroup으로 세분화하기
-- Q. 각 부서(s_emp.dept_id) 내에 직책(s_emp.title)별로 몇 명의 인원이 있는지 나타내라.

SELECT
    dept_id,
    title,
    COUNT(*) AS people
FROM
    s_emp -- '*'을 찍으면 행의 개수를 가지고 작업한다 <-> 특정 컬럼(ex) name)을 찍으면 null은 제외하고 작업한다
GROUP BY
    dept_id,
    title
ORDER BY
    1 ASC,
    2 DESC;

-- Q. 각 부서(s_emp.dept_id)마다 직원이 몇 명있는지 나타내라.

SELECT
    dept_id,
    COUNT(*)
FROM
    s_emp
GROUP BY
    dept_id
ORDER BY
    1;

-- Q. 각 부서(s_emp.dept_id)마다 급여(s_emp.salary)의 최소 & 최대를 구하고, 최소 & 최대가 같은 부서(s_emp.dept_id)를 제외한 나머지 부서(s_emp.dept_id)를 나타내라.

SELECT
    dept_id,
    MAX(salary),
    MIN(salary)
FROM
    s_emp
GROUP BY
    dept_id
HAVING
    MAX(salary) NOT LIKE MIN(salary) -- '<>'는 '!='와 같다
ORDER BY
    1;


-- Q. join 사용하기

SELECT
    s_dept.id,
    s_dept.name,
    s_region.name   AS region_name
FROM
    s_dept,
    s_region
WHERE
    s_dept.region_id = s_region.id;

-- Q. 사원(s_emp) 부서(s_dept) 지역(s_region)을 같이 출력하라
    -- 사원이름 --> s_emp.name
    -- 부서이름 --> s_dept.name
    -- 지역이름 --> s_region.name

SELECT
    s_emp.name      AS name,
    s_dept.name     AS dept -- 쉼표를 앞에 붙이는 이유는, 이러면 한 줄을 주석처리해도 앞뒤 줄이 문제 없이 연결되기 때문이다.
    ,
    s_region.name   AS region -- <table name>.* --> 해당 테이블의 모든 컬럼을 select한다.
FROM
    s_emp,
    s_dept,
    s_region
WHERE
    s_emp.dept_id = s_dept.id
    AND s_dept.region_id = s_region.id
ORDER BY
    s_region.name;
    
    
-- Q. 서울 지역에 근무하는 사원에 대해 각 사원의 이름과 근무하는 부서명을 나타내라.
    -- 서울지역 --> s_region.id = 1 = s_dept.region_id
    -- 사원이름 --> s_emp.name
    -- 각 사원의 부서명 --> s_dept.name, s_emp.dept_id

SELECT
    e.name   AS name,
    d.name   AS dept
FROM
    s_region r, -- s_region AS r와 같다
    s_emp e,
    s_dept d
WHERE
    r.name = '서울특별시'
    AND r.id = d.region_id
    AND d.id = e.dept_id
ORDER BY
    d.name;

-- NON-EQUIJOIN

SELECT
    e.name,
    e.salary,
    g.grade   급여등급
FROM
    s_emp e,
    salgrade g
WHERE
    e.salary BETWEEN g.losal AND g.hisal;
    
-- OUTER JOIN
-- 담당사원이 배정되지 않은 고객의 명단을 출력하라.

SELECT
    e.name   사원명,
    e.id,
    c.name   고객명
FROM
    s_emp e,
    s_customer c
WHERE
    e.id (+) = c.sales_rep_id -- OUTER JOIN 쓰기 위해서는 "(+)"를 한 쪽에 붙인다. --> 없는 것이 있는 쪽에 붙인다 (이 예시의 경우, 담당사원이 없는 쪽에 붙인다)
    AND e.name IS NULL        -- "(+)"을 어느쪽에 붙이는지에 따라 결과가 다르다
ORDER BY
    2;
    
-- Q. 이번 달의 마지막 날 구하기
    -- 다음 달의 1일에서 -1

SELECT
    last_day(SYSDATE)
FROM
    dual;

SELECT
    trunc(add_months(SYSDATE, 1), 'month') - 1
FROM
    dual;


-- 11월 29일 실습 문제 풀기

-- Q. 각 부서(dept_id)별 평균 급여를 계산해서 보여라.

SELECT
    dept_id,
    round(AVG(salary), 2)
FROM
    s_emp
GROUP BY
    dept_id
ORDER BY
    AVG(salary) DESC;

-- Q. 각 부서(dept_id)별로 직책이 사원인 직원들의 평균급여를 계산하라.

SELECT
    dept_id,
    round(AVG(salary), 2)
FROM
    s_emp
WHERE
    title = '사원'
GROUP BY
    dept_id
ORDER BY
    AVG(salary) DESC;

-- Q. 각 지역(region_id)별로 몇 개의 부서가 있는지를 나타내라.

SELECT
    region_id,
    COUNT(*) AS dept
FROM
    s_dept
GROUP BY
    region_id;

-- Q. 각 부서(dept_id)별로 평균 급여(salary)를 구하되 평균 급여가 2000 이상인 부서만 나타내라.

SELECT
    dept_id,
    round(AVG(salary), 2)
FROM
    s_emp
GROUP BY
    dept_id
HAVING
    AVG(salary) >= 2000
ORDER BY
    AVG(salary) DESC;

-- Q.각 직책(title)별로 급여의 총합을 구하되 직책이 부장인 사람은 제외하라.
    -- 단 급여총합이 8000 이상인 직책만 나타내며, 급여총합에 대한 오름차순으로 정렬하라.

SELECT
    title,
    SUM(salary)
FROM
    s_emp
GROUP BY
    title
HAVING title NOT LIKE '%부장'
       AND SUM(salary) >= 8000
ORDER BY
    SUM(salary);
    
-- Q. 각 부서별로 직책이 사원인 직원들만 평균 급여를 구하라.

SELECT
    dept_id,
    SUM(salary)
FROM
    s_emp
WHERE
    title = '사원'
GROUP BY
    dept_id
ORDER BY
    SUM(salary);

-- Q. 각 부서 내에서 각 직책별로 몇 명의 인원이 있는지를 나타내라

SELECT
    dept_id,
    title,
    COUNT(*) AS people
FROM
    s_emp
GROUP BY
    dept_id,
    title
ORDER BY
    dept_id;

-- Q. 각 부서내에서 몇 명의 직원이 근무하는지를 나타내라.

SELECT
    dept_id,
    COUNT(*) AS people
FROM
    s_emp
GROUP BY
    dept_id
ORDER BY
    dept_id;

-- Q. 각 부서별로 급여의 최소값과 최대값을 나타내라.
    -- 단, 최소값과 최대값이 같은 부서는 출력하지 마라.

SELECT
    dept_id,
    MIN(salary),
    MAX(salary)
FROM
    s_emp
GROUP BY
    dept_id
HAVING
    MIN(salary) != MAX(salary)
ORDER BY
    dept_id;

-- Q. OUTER JOIN을 이용하여 담당 고객이 배정되지 않은 직원의 퍼센트를 출력하라. -- 나중에! 아직은 무리...
--Select count(e.name) from s_emp e, s_customer c
--Where e.id (+) = c.sales_rep_id
--Order by 1
--;

-- SELF JOIN

SELECT
    w.id     사번,
    w.name   사원명,
    m.id     부서장사번,
    m.name   부서장명
FROM
    s_emp w,
    s_emp m
WHERE
    w.manager_id = m.id;

-- Q. 직원 중 '김정미'와 같은 직책(title)을 가지는 직원의 이름과 직책, 급여, 부서번호를 나타내라 (SELF JOIN 사용하라)

SELECT
    w.name,
    w.title,
    w.salary,
    w.dept_id
FROM
    s_emp w,
    s_emp s
WHERE
    s.title = w.title
    AND s.name = '김정미'
--    AND s.name <> w.name;
    AND w.name <> '김정미';

-- SET 연산자 사용하기

SELECT
    name,
    dept_id,
    title
FROM
    s_emp
WHERE
    dept_id = 110
UNION
SELECT
    name,
    dept_id,
    title
FROM
    s_emp
WHERE
    dept_id = 113
ORDER BY
    2,
    3;
    
-- Q. 과장 평균 연봉과 사원 평균 연봉을 나란히 출력하라.
    -- SELF JOIN으로!

SELECT
    e1.title   AS 직책,
    AVG(e1.salary) AS 평균연봉,
    e2.title   AS 직책,
    AVG(e2.salary) AS 평균연봉
FROM
    s_emp e1,
    s_emp e2
WHERE
    e1.title = '과장'
    AND e2.title = '사원'
GROUP BY
    e1.title,
    e2.title;
    
    -- SUBQUERY으로!

SELECT
    title01    AS 직책_1,
    salary01   AS 평균연봉_1,
    title02    AS 직책_2,
    salary02   AS 평균연봉_2
FROM
    (
        SELECT
            title   AS title01,
            AVG(salary) AS salary01
        FROM
            s_emp d
        WHERE
            title = '과장'
        GROUP BY
            title
    ),
    (
        SELECT
            title   AS title02,
            AVG(salary) AS salary02
        FROM
            s_emp e
        WHERE
            title = '사원'
        GROUP BY
            title
    );

    -- SUBQUERY & UNION ALL으로!

SELECT
    title1,
    SUM(sal1),
    title2,
    SUM(sal2)
FROM
    (
        SELECT
            title   title1,
            AVG(salary) sal1,
            '사원' title2,
            0 sal2
        FROM
            s_emp --> title2, sal2는 가상 컬럼
        WHERE
            title IN (
                '과장'
            )
        GROUP BY
            title
        UNION ALL
        SELECT
            '과장' title1,
            0 sal1,
            title   title2,
            AVG(salary) sal2
        FROM
            s_emp --> title1, sal1는 가상 컬럼
        WHERE
            title IN (
                '사원'
            )
        GROUP BY
            title
    )
GROUP BY
    title1,
    title2;
-- Q. DECODE로 직책별 평균연봉을 출력하라
    -- 출력 예) 컬럼 --> 사원 과장 부장 이사 사장 <-- 헤더 이름
--                      800 1000 1500 etc.

SELECT
    AVG(DECODE(substr(title, - 2, 2), '사원', salary)) "사원", --> title이 사원이면 salary를 값으로, 아니면 0으로
    AVG(DECODE(substr(title, - 2, 2), '과장', salary)) "과장",
    AVG(DECODE(substr(title, - 2, 2), '부장', salary)) "부장",
    AVG(DECODE(substr(title, - 2, 2), '이사', salary)) "이사",
    AVG(DECODE(substr(title, - 2, 2), '사장', salary)) "사장"
FROM
    s_emp;

SELECT
    substr('영업부장', - 2, 2)
FROM
    dual;

-- SUBQUERY --> subquery부분만 하이라이트하고 ctrl+enter 치면 그 부분만 실행할 수 있다

SELECT
    name,
    title,
    dept_id
FROM
    s_emp
WHERE
    dept_id = (
        SELECT
            dept_id
        FROM
            s_emp
        WHERE
            name = '김정미'
    );

-- MULTI ROW SUBQUERY

SELECT
    name,
    dept_id
FROM
    s_emp
WHERE
    dept_id IN ( -- subquery의 select절 내용하고 query의 where절 내용이 일치해야 한다
        SELECT
            id
        FROM
            s_dept
        WHERE
            region_id = 3
    );

-- Having절에서의 SUBQUERY
-- Q. 가장 적은 평균급여를 받는 직책에 대해 그 직책과 평균급여를 출력하라.

SELECT
    title,
    AVG(salary)
FROM
    s_emp
GROUP BY
    title
HAVING
    AVG(salary) = (
        SELECT
            MIN(AVG(salary))
            -- title, MIN(AVG(salary))는 에러난다! --> title당 하나의 min으로 만들어지기에 이미 group화가 끝났다 --> GROUP BY가 필요없다
        FROM
            s_emp
        GROUP BY
            title
    );
    
-- Create절에서의 SUBQUERY

CREATE TABLE emp_113 (
    id,
    name,
    mailid,
    start_date
)
    AS
        SELECT
            id,
            name,
            mailid,
            start_date
        FROM
            s_emp
        WHERE
            dept_id = 113; -- "where 1 <> 1"라고 하면 항상 false이기에 아무것도 안 가져온다


-- DML문에서의 SUBQUERY

INSERT INTO emp_115 (
    id,
    name,
    mailid,
    start_date
)
    SELECT
        id,
        name,
        mailid,
        start_date
    FROM
        s_emp
    WHERE
        start_date < '16/01/01';

UPDATE s_emp
SET
    dept_id = (
        SELECT
            dept_id
        FROM
            s_emp
        WHERE
            title = '사장'
    )
WHERE
    name = '안창환';

-- Dictionary 관련

DESC dictionary;

SELECT
    *
FROM
    dictionary;

SELECT
    *
FROM
    dictionary
WHERE
    table_name LIKE 'USER%';

SELECT
    *
FROM
    user_objects
WHERE
    object_type = 'TABLE';

SELECT
    *
FROM
    user_constraints
WHERE
    table_name = 'S_EMP';
    
-- 내가 (현재 connect된 사용자가) 가진 모든 테이블 출력

SELECT
    *
FROM
    tabs;

-- SEQUENCE 생성하기

CREATE SEQUENCE c_emp_id INCREMENT BY 1 START WITH 26 MAXVALUE 9999999 NOCACHE NOCYCLE;

-- SEQUENCE 생성 확인하기

SELECT
    sequence_name,
    min_value,
    max_value,
    increment_by,
    last_number
FROM
    user_sequences;
    
-- SEQUENCE 사용 예제
    -- S_EMP 테이블에서 이름은 홍길동, 급여는 2000, 나머지는 null을 입력하되, 사번은 sequence값을 사용하라.
INSERT INTO s_emp (
    id,
    name,
    salary
) VALUES (
    c_emp_id.NEXTVAL,
    '홍길동' || c_emp_id.NEXTVAL,
    2000
);
select * from s_emp;

SELECT
    *
FROM
    s_emp;

INSERT INTO s_emp (
    id,
    name,
    salary
)
    SELECT
        c_emp_id.NEXTVAL,
        name || c_emp_id.NEXTVAL,
        salary
    FROM
        s_emp;

-- 인텍스 idx 만들기

CREATE INDEX idx ON
    s_emp (
        name
    );
--인덱스 생성한 것 확인하기

SELECT
    *
FROM
    user_indexes;

-- 뷰 만들기

CREATE VIEW vw_emp AS
    SELECT
        e.name,
        e.mailid,
        d.name   AS dept_name
    FROM
        s_emp e,
        s_dept d
    WHERE
        e.dept_id = d.id;
-- 뷰 보기

SELECT
    *
FROM
    vw_emp; -- 실행하면 vw_emp의 query 내용(내가 작성한 SQL 내용)이 SUBQUERY로 실행된다

SELECT
    *
FROM
    user_views;

-- SYNONYM 만들기

CREATE SYNONYM ve FOR vw_ewp;

-- DECODE

SELECT
    name,
    salary,
    trunc(salary / 1000) AS 급여,
    DECODE(trunc(salary / 1000), 0, 'E', 1, 'D', 2, 'C', 3, 'B', 'A') 급여등급
    -- 'A'가 default값, "급여등급"은 ALIAS
FROM
    s_emp;

-- 위 DECODE를 대신 CASE로!

SELECT
    name,
    CASE trunc(salary / 1000)
        WHEN 0   THEN 'E'
        WHEN 1   THEN 'D'
        WHEN 2   THEN 'C'
        WHEN 3   THEN 'B'
        ELSE 'A'
    END AS 급여수준
FROM
    s_emp;
    
-- PIVOT함수

SELECT
    *
FROM
    (
        SELECT
            dept_id,
            title
        FROM
            s_emp
    ) PIVOT (
        COUNT ( * )
        FOR title
        IN ( '사원',
        '과장',
        '부장',
        '이사',
        '사장' )
    )
ORDER BY
    dept_id;
    
-- 위 PIVOT함수 내용을 대신 DECODE로!

SELECT
    dept_id,
    COUNT(DECODE(title, '사원', 0)) "사원",
    COUNT(DECODE(title, '과장', 0)) "과장",
    COUNT(DECODE(title, '부장', 0)) "부장",
    COUNT(DECODE(title, '이사', 0)) "이사",
    COUNT(DECODE(title, '사장', 0)) "사장"
FROM
    s_emp
GROUP BY
    dept_id
ORDER BY
    dept_id;
    
-- CORRELATED SUBQUERY

SELECT
    name,
    salary,
    dept_id
FROM
    s_emp outer
WHERE
    salary < (
        SELECT
            AVG(salary)
        FROM
            s_emp
        WHERE
            dept_id = outer.dept_id
    );
    
-- Multi Row Comparison Operator ANY

SELECT
    name,
    salary,
    dept_id
FROM
    s_emp
WHERE
    salary < ANY (
        SELECT
            AVG(salary)
        FROM
            s_emp
        GROUP BY
            dept_id
    );
    
-- Multi Row Comparison Operator EXISTS

SELECT
    id,
    name,
    title,
    dept_id
FROM
    s_emp e
WHERE
    EXISTS (
        SELECT
            id
        FROM
            s_emp
        WHERE
            manager_id = e.id
    );

-- ROWID 출력
select rowid, id, name from s_emp; --> 출력했을 때 마지막 한 글자만 다르다 --> 한 글자만 차이난다는 의미는 메모리 상에 서로 옆에 붙어있다는 뜻
select rowid, rownum, id, name from s_emp; --> rownum이 순서대로 나온다 --> rownum & id가 숫자가 같다 --> 별도로 order by로 정렬하지 않는 이상, 처음부터 끝까지 읽어오는 것이 가장 빠르기 때문 --> 정렬해도, 먼저 일어온 후 정렬을 하기 때문에 rownum이 뒤죽박죽이어야 한다
-- rowid는 64진수로 표현 --> 보다 짧가 표현하려고 (64진수 = base-64)
select rowid, rownum, id, name from s_emp
order by name;

-- 직원(s_emp)테이블에서 이름을 사전순으로 정렬하여 5개의 데이터만 나타내라.
select name from s_emp
where rownum <= 5
order by name;

-- Q. 최고 연봉자 7명을 출력하라. -- SUBQUERY 사용
select * from (select rownum, name, salary from s_emp
--where rownum <= 7 --> NO!
order by salary desc)
where rownum <= 7; --> SUBQUERY의 rownum과 다른, SUBQUERY의 정렬된 결과물을 다시 rownum 매긴 것!

-- INDEX의 효율적인 사용 --> 부정조건을 기술한 경우
create index s_emp_title_idx on s_emp (title);

-- 결과는 같지만 index 쓸 때가 더 빠르다 --> 인덱스를 타는지 안타는지 보기위해서는 "실행계획" (F10) 누르면 "INDEX"라고 써있다
select id, name, title from s_emp where title <> '사원';
select id, name, title from s_emp e
where not exists (Select 'x' from s_emp where e.title = '사원');
-- 'x'는 의미없다 --> 문제는 select 뒤에 무엇인가는 써줘야하기에, 'x'를 쓴 것 --> '1'써도 똑같고, 상관없다 --> title을 써도 문제 없다
select 1, 'x', name from s_emp; --> s_emp의 row 개수만큼 1과 'x'가 출력된다 --> 1이나 'x'는 값이 있다는 의미의 상수 같은 것
--> 숫자보다 문자가 빠르기에 'x'가 더 좋다

-- QUIZ 01. 각 부서별 평균 급여를 구하되 평균 급여가 2000 이상인 부서만 나타내는 SELECT문을 작성하라.
SELECT dept_id, avg(salary) FROM s_emp
WHERE dept_id IS NOT NULL --> dept_id가 null값인 경우를 제외하기 위해서 추가
GROUP BY dept_id
HAVING avg(salary) >= 2000
ORDER BY 2 desc;

-- QUIZ 02. 각 직책(s_emp.title)별로 급여(s_emp.salary)의 총합을 구하되 직책이 사원인 사람은 제외하고, 급여총합이 3000 이상인 직책만 나타내며, 급여 총합에 대한 내림차순으로 정렬하라.
SELECT title, sum(salary) FROM s_emp
WHERE title <> '사원'
GROUP BY title
HAVING sum(salary) >= 3000
ORDER BY 2 desc;

-- QUIZ 03. 직급(s_emp.title)이 '부장'인 사람이 2명 이하인 부서(s_emp.dept_id)가 몇 개인지 나타내는 SELECT문을 작성하라.
SELECT count(*) FROM (SELECT dept_id, count(*) FROM s_emp
    WHERE title LIKE '%부장'
    GROUP BY dept_id
    HAVING count(*) <= 2)
;

-- QUIZ 04. 담당직원(s_customer.sales_rep_id)이 배정되지 않은 고객(s_customer.name)을 모두 나타내는 SELECT문을 작성하라.
SELECT e.name 담당직원, c.name 고객 FROM s_emp e, s_customer c
WHERE e.id (+) = c.sales_rep_id --> 고객은 있으나 담당직원이 없는 경우를 찾는다 --> c.name은 있으나 e.name은 없는 경우를 찾는다 --> e에다가 (+) 추가해야!
AND e.name IS NULL;

-- QUIZ 05. Primary Key & Foreign Key에 대해 설명하라.
    --PK : 테이블의 각 행을 구별할 수 있게 하는 컬럼
         --PK는 NOT NULL & UNIQUE 해야 한다
         --PK는 자동으로 인덱스가 생성된다
    --FK : 다른 테이블의 PK
         --다른 테이블의 PK컬럼값 중의 하나이거나 NULL이어야 한다

-- QUIZ 06. CONSTRAINT의 종료를 모두 적어라 (총 5개)
    --CONSTRAINT는 제약조건으로 잘못된 데이터가 입력되는 것을 막는다.
    --NOT NULL : 필수 입력값
    --UNIQUE   : 중복값 허용 안함
    --PK       : NOT NULL + UNIQUE처럼 작용
    --FK
    --CHECK    : 컬럼에 조건을 지정

-- QUIZ 08. 전체 임직원 중에서 연봉이 제일 높은 사람 3명의 이름과 연봉을 출력하는 문장을 작성하라.
SELECT * FROM --> FROM에서 벌써 name과 salary만 주기 때문에 * 사용
    (SELECT name, salary
    FROM s_emp
    ORDER BY salary desc)
WHERE rownum <= 3;

-- QUIZ 10. 아래의 SQL문은 비효율적이다. 이 문장을 NOT EXISTS를 사용해서 효울적으로 작성하라.
    -- >> select id, name, title from s_emp where title <> '사원';
        -- 인덱스를 사용하지 않는다!
            -- 인덱스를 사용하지 않는 경우
                -- 1. 컬럼이 변경된 경우
                -- 2. 부정조건인 경우
                -- 3. NULL과 비교하는 경우
                -- 4. Optimizer의 취사 선택의 경우
SELECT * FROM s_emp e
WHERE NOT EXISTS
    (SELECT 'x'
    FROM s_emp
    WHERE e.title = '사원');

-- INDEX    
-- 현 사용자가 가진 인덱스 모두 출력
SELECT * FROM ind
WHERE table_name = 'S_EMP'; --> 이 줄을 추가하면 특정 테이블과 연관된 인덱스만 출력

-- 인덱스는 이진트리(B*Tree)를 사용한다 --> 정렬과 범위검색에 유리 (tree 자료구조 자체의 특성)
SELECT * FROM s_emp
ORDER BY name;

SELECT /*+INDEX_ASC(S_EMP S_EMP_TITLE_IDX)*/ --> Oracle Hint는 주석의 형태를 가진다 --> 오타 나도 알 수가 없다
* FROM S_EMP;

-- CONNECT BY는 Oracle 11부터 생긴 기능
SELECT level-5 FROM dual
CONNECT BY level <= 10;

-- CONNECT BY를 이용하여 최근 12개월의 날짜를 YYYYMM 형태로 출력하라.
SELECT TO_CHAR(ADD_MONTHS(SYSDATE, -12 + LEVEL), 'YYYYMM') AS YYYYMM FROM DUAL
CONNECT BY LEVEL <= 12;

-- 과제 -- 직원정보(s_emp) 테이블에서 입사일(start_date)이 2015년인 직원들의 수를 출력하라.
    -- 단, 출력형태는 월별로 그룹화하여 해당 우러에 입사한 직원의 수가 출력되어야 한다.
    -- 입사자가 없는 월도 출력되어야 하며, 입사자의 수는 0으로 표시해야 한다.
SELECT "입사월", COUNT(*) "입사직원수" FROM (SELECT TO_CHAR(START_DATE, 'YYYYMM') "입사월" FROM S_EMP
GROUP BY START_DATE
HAVING TO_CHAR(START_DATE, 'YYYY') = 2015)
GROUP BY "입사월"
ORDER BY 1;

-- 미완성.... 3,4,5월이 중복된다
SELECT TO_CHAR(ADD_MONTHS('15/12/01', -12 + LEVEL), 'YYYYMM') AS "입사월", 0 AS "입사직원수" FROM DUAL
CONNECT BY LEVEL <= 12
GROUP BY TO_CHAR(ADD_MONTHS('15/12/01', -12 + LEVEL), 'YYYYMM')
UNION
SELECT "입사월", COUNT(*) "입사직원수" FROM (SELECT TO_CHAR(START_DATE, 'YYYYMM') "입사월" FROM S_EMP
GROUP BY START_DATE
HAVING TO_CHAR(START_DATE, 'YYYY') = 2015)
GROUP BY "입사월"
ORDER BY 1;


SELECT "입사", COUNT("입사월") "입사직원수" FROM (SELECT TO_CHAR(ADD_MONTHS('15/12/01', -12 + LEVEL), 'YYYYMM') AS "입사" FROM DUAL
CONNECT BY LEVEL <= 12
GROUP BY TO_CHAR(ADD_MONTHS('15/12/01', -12 + LEVEL), 'YYYYMM')
ORDER BY 1), (SELECT TO_CHAR(START_DATE, 'YYYYMM') "입사월" FROM S_EMP
GROUP BY START_DATE
HAVING TO_CHAR(START_DATE, 'YYYY') = 2015)
GROUP BY "입사"
ORDER BY 1;

SELECT TO_CHAR(ADD_MONTHS('15/12/01', -12 + LEVEL), 'YYYYMM') AS YYYYMM FROM DUAL
CONNECT BY LEVEL <= 12;





SELECT 
    title1,
    SUM(sal1),
    title2,
    SUM(sal2)
FROM
    (
        SELECT
            title   title1,
            AVG(salary) sal1,
            '사원' title2,
            0 sal2
        FROM
            s_emp --> title2, sal2는 가상 컬럼
        WHERE
            title IN (
                '과장'
            )
        GROUP BY
            title
        UNION ALL
        SELECT
            '과장' title1,
            0 sal1,
            title   title2,
            AVG(salary) sal2
        FROM
            s_emp --> title1, sal1는 가상 컬럼
        WHERE
            title IN (
                '사원'
            )
        GROUP BY
            title
    )
GROUP BY
    title1,
    title2;