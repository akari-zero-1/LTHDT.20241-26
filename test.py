import os

def count_java_lines(directory):
    total_lines = 0
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith(".java"):
                with open(os.path.join(root, file), "r", encoding="utf-8") as f:
                    total_lines += sum(1 for _ in f)
    return total_lines

directory = input("Nhập đường dẫn thư mục: ")
print(f"Tổng số dòng Java: {count_java_lines(directory)}")
